package com.nerdscorner.mvp.ui;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.domain.ExecutionResult;
import com.nerdscorner.mvp.domain.manifest.Manifest;
import com.nerdscorner.mvp.mvp.ActivityMvpBuilder;
import com.nerdscorner.mvp.mvp.FragmentMvpBuilder;
import com.nerdscorner.mvp.mvp.MvpBuilder;
import com.nerdscorner.mvp.utils.Constants;
import com.nerdscorner.mvp.utils.Constants.Properties;
import com.nerdscorner.mvp.utils.gradle.GradleUtils;
import com.nerdscorner.mvp.utils.ManifestUtils;
import com.nerdscorner.mvp.utils.StringUtils;


import static com.nerdscorner.mvp.utils.gradle.GradleUtils.COROUTINES_LIB_EVENTS_DEPENDENCY_PKG;
import static com.nerdscorner.mvp.utils.gradle.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY_PKG;

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
    private JCheckBox includeLibraryDependency;
    private JCheckBox includeCoroutinesLibraryDependency;
    private JRadioButton activityRadioButton;
    private JRadioButton fragmentRadioButton;
    private JRadioButton javaRadioButton;
    private JRadioButton kotlinRadioButton;
    private JCheckBox addSampleCode;

    public PackageAndScreenInputDialog(Project project, VirtualFile rootFolder, AnActionEvent actionEvent) {
        this.project = project;
        this.rootFolder = rootFolder;
        this.actionEvent = actionEvent;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        restoreInputStates();

        addWindowListener(new WindowAdapter() {
            // call onCancel() when cross is clicked
            public void windowClosing(WindowEvent e) {
                onCancel();
            }

            // Request focus on the screen name input
            public void windowOpened(WindowEvent e) {
                screenName.requestFocus();
            }
        });
    }


    public JButton getButtonOK() {
        return buttonOK;
    }

    private void onOK() {
        boolean activity = activityRadioButton.isSelected();
        boolean shouldIncludeLibraryDependency = includeLibraryDependency.isSelected() && !isMvpLibInstalled();
        boolean shouldIncludeCoroutinesLibraryDependency = includeCoroutinesLibraryDependency.isSelected() && !isCoroutinesLibInstalled();
        boolean shouldCreateWiring = addSampleCode.isSelected();

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
            mvpBuilder = new ActivityMvpBuilder(
                    shouldIncludeLibraryDependency,
                    shouldIncludeCoroutinesLibraryDependency,
                    javaRadioButton.isSelected(),
                    shouldCreateWiring
            );
        } else {
            mvpBuilder = new FragmentMvpBuilder(
                    shouldIncludeLibraryDependency,
                    shouldIncludeCoroutinesLibraryDependency,
                    javaRadioButton.isSelected(),
                    shouldCreateWiring
            );
        }
        ExecutionResult executionResult = mvpBuilder.build(rootFolder, basePath, basePackage, screenName);

        if (executionResult.getSuccessful() && (shouldIncludeLibraryDependency || shouldIncludeCoroutinesLibraryDependency)) {
            GradleUtils.performSync(actionEvent);
        }

        onCancel();

        //Show result
        ResultDialog resultDialog = new ResultDialog(
                executionResult.getSuccessful() ?
                        "MVP created successfully" :
                        "An error occurred while creating files:" + executionResult.getChainedMessages()
        );
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

    private boolean isMvpLibInstalled() {
        return GradleUtils.hasDependency(rootFolder, MVP_LIB_EVENTS_DEPENDENCY_PKG);
    }

    private boolean isCoroutinesLibInstalled() {
        return GradleUtils.hasDependency(rootFolder, COROUTINES_LIB_EVENTS_DEPENDENCY_PKG);
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
    }

    private void restoreInputStates() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);

        //Package name
        Manifest manifest = ManifestUtils.getManifest(rootFolder);
        String savedPackageName = propertiesComponent.getValue(Constants.Properties.PROPERTY_PACKAGE_NAME, "");
        packageName.setText(manifest == null ? savedPackageName : manifest.getPkg());

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
    }
}
