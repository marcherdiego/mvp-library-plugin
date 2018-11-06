package com.nerdscorner.mvp.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.domain.manifest.Activity;
import com.nerdscorner.mvp.domain.manifest.Fragment;
import com.nerdscorner.mvp.domain.manifest.Manifest;
import com.nerdscorner.mvp.domain.manifest.ScreenComponent;
import com.nerdscorner.mvp.mvp.ActivityMvpBuilder;
import com.nerdscorner.mvp.mvp.FragmentMvpBuilder;
import com.nerdscorner.mvp.mvp.MvpBuilder;
import com.nerdscorner.mvp.utils.Constants;
import com.nerdscorner.mvp.utils.Constants.Properties;
import com.nerdscorner.mvp.utils.GradleUtils;
import com.nerdscorner.mvp.utils.ListUtils;
import com.nerdscorner.mvp.utils.ManifestUtils;
import com.nerdscorner.mvp.utils.ProjectUtils;
import com.nerdscorner.mvp.utils.StringUtils;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY_PKG;
import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_INTERFACES_DEPENDENCY_PKG;

public class PackageAndScreenInputDialog extends JDialog {
    private static final String ACTIVITY = "Activity";
    private static final String FRAGMENT = "Fragment";

    private final Project project;
    private final VirtualFile rootFolder;
    private AnActionEvent actionEvent;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private javax.swing.JTextField packageName;
    private javax.swing.JTextField screenName;
    private JRadioButton interfacesRadioButton;
    private JCheckBox includeLibraryDependency;
    private JRadioButton activityRadioButton;
    private JRadioButton fragmentRadioButton;
    private JRadioButton javaRadioButton;
    private JRadioButton kotlinRadioButton;
    private JRadioButton eventsRadioButton;
    private JComboBox existingActivity;
    private JComboBox existingFragment;

    public PackageAndScreenInputDialog(Project project, VirtualFile rootFolder, AnActionEvent actionEvent) {
        this.project = project;
        this.rootFolder = rootFolder;
        this.actionEvent = actionEvent;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        loadActivities();
        loadFragments();
        restoreInputStates();
    }

    private void loadActivities() {
        Activity[] activities = ManifestUtils.findActivities(rootFolder);
        if (activities == null) {
            existingActivity.setEnabled(false);
            return;
        }
        existingActivity.setEnabled(true);
        DefaultComboBoxModel<Activity> activitiesModel = new DefaultComboBoxModel<>();
        Activity selectActivity = new Activity();
        selectActivity.setName(ScreenComponent.CHOOSE_ONE);
        activitiesModel.addElement(selectActivity);
        for (Activity activity : activities) {
            activitiesModel.addElement(activity);
        }
        existingActivity.setModel(activitiesModel);
        existingActivity.addActionListener(e -> screenName.setEnabled(existingActivity.getSelectedIndex() == 0));
    }

    private void loadFragments() {
        List<Fragment> fragments = new LinkedList<>();
        ProjectUtils.getFragments(rootFolder, fragments);
        if (ListUtils.isEmpty(fragments)) {
            existingFragment.setEnabled(false);
            return;
        }
        existingFragment.setEnabled(true);
        DefaultComboBoxModel<Fragment> fragmentsModel = new DefaultComboBoxModel<>();
        Fragment selectFragment = new Fragment();
        selectFragment.setName(ScreenComponent.CHOOSE_ONE);
        fragmentsModel.addElement(selectFragment);
        for (Fragment fragment : fragments) {
            fragmentsModel.addElement(fragment);
        }
        existingFragment.setModel(fragmentsModel);
        existingFragment.addActionListener(e -> screenName.setEnabled(existingFragment.getSelectedIndex() == 0));
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    private void onOK() {
        boolean interfaces = interfacesRadioButton.isSelected();
        boolean activity = activityRadioButton.isSelected();
        boolean shouldIncludeLibraryDependency = false;
        if (includeLibraryDependency.isSelected()) {
            shouldIncludeLibraryDependency = !isMvpLibInstalled(interfaces);
        }

        String basePackage = packageName.getText();
        String screenName = StringUtils.asCamelCase(this.screenName.getText());
        if (StringUtils.isEmpty(basePackage) || StringUtils.isEmpty(screenName)) {
            //Show result
            ResultDialog resultDialog = new ResultDialog("Please enter a valid package and screen name");
            resultDialog.pack();
            resultDialog.setLocationRelativeTo(null);
            resultDialog.setTitle("MVP Builder Error");
            resultDialog.setResizable(false);
            resultDialog.setVisible(true);
            return;
        }
        screenName = sanitizeScreenName(screenName);
        String basePath = rootFolder.getPath() + File.separator + basePackage.replace(".", File.separator);
        MvpBuilder mvpBuilder;
        if (activity) {
            mvpBuilder = new ActivityMvpBuilder(shouldIncludeLibraryDependency, javaRadioButton.isSelected());
        } else {
            mvpBuilder = new FragmentMvpBuilder(shouldIncludeLibraryDependency, javaRadioButton.isSelected());
        }
        boolean success = mvpBuilder.build(rootFolder, basePath, basePackage, screenName, interfaces);

        if (success && shouldIncludeLibraryDependency) {
            GradleUtils.performSync(actionEvent);
        }

        onCancel();

        //Show result
        ResultDialog resultDialog = new ResultDialog(success ? "MVP created successfully" : "An error occurred while creating files.");
        resultDialog.pack();
        resultDialog.setLocationRelativeTo(null);
        resultDialog.setTitle("MVP Builder");
        resultDialog.setResizable(false);
        resultDialog.setVisible(true);

        //Save plugin state
        saveInputStates();
    }

    private String sanitizeScreenName(String screenName) {
        if (screenName.endsWith(ACTIVITY)) {
            int activityIndex = screenName.lastIndexOf(ACTIVITY);
            return screenName.substring(0, activityIndex);
        } else if (screenName.endsWith(FRAGMENT)) {
            int fragmentIndex = screenName.lastIndexOf(FRAGMENT);
            return screenName.substring(0, fragmentIndex);
        }
        return screenName;
    }

    private void onCancel() {
        setVisible(false);
        dispose();
    }

    private boolean isMvpLibInstalled(boolean interfaces) {
        return GradleUtils.hasDependency(rootFolder, interfaces ? MVP_LIB_INTERFACES_DEPENDENCY_PKG : MVP_LIB_EVENTS_DEPENDENCY_PKG);
    }

    private void saveInputStates() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);

        //Package name
        propertiesComponent.setValue(Constants.Properties.PROPERTY_PACKAGE_NAME, packageName.getText());

        //Activity or Fragment
        if (activityRadioButton.isSelected()) {
            propertiesComponent.setValue(Properties.PROPERTY_COMPONENT_TYPE, Properties.COMPONENT_TYPE_ACTIVITY);
        } else if (fragmentRadioButton.isSelected()) {
            propertiesComponent.setValue(Properties.PROPERTY_COMPONENT_TYPE, Properties.COMPONENT_TYPE_FRAGMENT);
        }

        //Java or Kotlin
        if (javaRadioButton.isSelected()) {
            propertiesComponent.setValue(Properties.PROPERTY_LANGUAGE, Properties.LANGUAGE_JAVA);
        } else if (kotlinRadioButton.isSelected()) {
            propertiesComponent.setValue(Properties.PROPERTY_LANGUAGE, Properties.LANGUAGE_KOTLIN);
        }

        //Events or Interfaces
        if (eventsRadioButton.isSelected()) {
            propertiesComponent.setValue(Properties.PROPERTY_COMMUNICATIONS, Properties.COMMUNICATIONS_EVENTS);
        } else if (interfacesRadioButton.isSelected()) {
            propertiesComponent.setValue(Properties.PROPERTY_COMMUNICATIONS, Properties.COMMUNICATIONS_INTERFACES);
        }
    }

    private void restoreInputStates() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);

        //Package name
        Manifest manifest = ManifestUtils.getManifest(rootFolder);
        String savedPackageName = propertiesComponent.getValue(Constants.Properties.PROPERTY_PACKAGE_NAME, "");
        packageName.setText(savedPackageName.equals("") ? (manifest == null ? "" : manifest.getPkg()) : savedPackageName);

        //Activity or Fragment
        String componentType = propertiesComponent.getValue(Properties.PROPERTY_COMPONENT_TYPE, Properties.COMPONENT_TYPE_ACTIVITY);
        if (componentType.equals(Properties.COMPONENT_TYPE_ACTIVITY)) {
            activityRadioButton.setSelected(true);
        } else if (componentType.equals(Properties.COMPONENT_TYPE_FRAGMENT)) {
            fragmentRadioButton.setSelected(true);
        }

        //Java or Kotlin
        String language = propertiesComponent.getValue(Properties.PROPERTY_LANGUAGE, Properties.LANGUAGE_JAVA);
        if (language.equals(Properties.LANGUAGE_JAVA)) {
            javaRadioButton.setSelected(true);
        } else if (language.equals(Properties.LANGUAGE_KOTLIN)) {
            kotlinRadioButton.setSelected(true);
        }

        //Events or Interfaces
        String communications = propertiesComponent.getValue(Properties.PROPERTY_COMMUNICATIONS, Properties.COMMUNICATIONS_EVENTS);
        if (communications.equals(Properties.COMMUNICATIONS_EVENTS)) {
            eventsRadioButton.setSelected(true);
        } else if (communications.equals(Properties.COMMUNICATIONS_INTERFACES)) {
            interfacesRadioButton.setSelected(true);
        }
    }
}
