package com.nerdscorner.mvp.events.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.events.domain.manifest.Activity;
import com.nerdscorner.mvp.events.domain.manifest.Manifest;
import com.nerdscorner.mvp.events.mvp.MvpBuilder;
import com.nerdscorner.mvp.events.utils.GradleUtils;
import com.nerdscorner.mvp.events.utils.ManifestUtils;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import static com.nerdscorner.mvp.events.utils.GradleUtils.LIBRARY_DEPENDENCY_PACKAGE;

public class PackageAndScreenInputDialog extends JDialog {
    private static final String PROPERTY_PACKAGE_NAME = "package_name";

    private final Project project;
    private final VirtualFile rootFolder;
    private AnActionEvent actionEvent;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private javax.swing.JTextField packageName;
    private javax.swing.JTextField screenName;
    private JComboBox<Activity> existingActivity;
    private JRadioButton interfacesRadioButton;
    private JCheckBox includeLibraryDependency;

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

        Manifest manifest = ManifestUtils.getManifest(rootFolder);

        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        String savedPackageName = propertiesComponent.getValue(PROPERTY_PACKAGE_NAME, "");
        packageName.setText(savedPackageName.equals("") ? (manifest == null ? "" : manifest.getPkg()) : savedPackageName);

        loadActivities(rootFolder, existingActivity);
        existingActivity.addActionListener(e -> screenName.setEnabled(existingActivity.getSelectedIndex() == 0));
    }

    private void loadActivities(VirtualFile rootFolder, JComboBox<Activity> comboBox) {
        Activity[] activities = ManifestUtils.findActivities(rootFolder);
        if (activities == null) {
            return;
        }
        DefaultComboBoxModel<Activity> activitiesModel = new DefaultComboBoxModel<>();
        Activity selectActivity = new Activity();
        selectActivity.setName("Choose one");
        activitiesModel.addElement(selectActivity);
        for (Activity activity : activities) {
            activitiesModel.addElement(activity);
        }
        comboBox.setModel(activitiesModel);
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    private void onOK() {
        boolean shouldIncludeLibraryDependency = false;
        if (includeLibraryDependency.isSelected()) {
            shouldIncludeLibraryDependency = !isMvpLibInstalled();
        }

        boolean isNewScreen = existingActivity.getSelectedIndex() == 0;
        String basePackage = packageName.getText();
        String screenName = isNewScreen ?
                this.screenName.getText() :
                ((Activity) existingActivity.getSelectedItem()).getName();
        String basePath = rootFolder.getPath() + File.separator + basePackage.replace(".", File.separator);
        boolean interfaces = interfacesRadioButton.isSelected();
        MvpBuilder mvpBuilder = new MvpBuilder(rootFolder, basePath, basePackage, screenName, interfaces, isNewScreen, shouldIncludeLibraryDependency);
        boolean success = mvpBuilder.build();

        if (success) {
            ActionManager am = ActionManager.getInstance();
            AnAction sync = am.getAction("Android.SyncProject");
            sync.actionPerformed(actionEvent);
        }

        //Show result
        ResultDialog resultDialog = new ResultDialog(success ? "MVP created successfully" : "An error occurred while creating files.");
        resultDialog.pack();
        resultDialog.setLocationRelativeTo(null);
        resultDialog.setTitle("MVP Builder");
        resultDialog.setResizable(false);
        resultDialog.setVisible(true);
        dispose();

        //Save plugin state
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        propertiesComponent.setValue(PROPERTY_PACKAGE_NAME, basePackage);
    }

    private void onCancel() {
        dispose();
    }

    private boolean isMvpLibInstalled() {
        return GradleUtils.hasDependency(rootFolder, LIBRARY_DEPENDENCY_PACKAGE);
    }
}
