package com.nerdscorner.mvp.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.domain.manifest.Manifest;
import com.nerdscorner.mvp.mvp.MvpBuilder;
import com.nerdscorner.mvp.utils.GradleUtils;
import com.nerdscorner.mvp.utils.ManifestUtils;
import com.nerdscorner.mvp.utils.StringUtils;

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

import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY_PKG;
import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_INTERFACES_DEPENDENCY_PKG;

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
    }

    public JButton getButtonOK() {
        return buttonOK;
    }

    private void onOK() {
        boolean interfaces = interfacesRadioButton.isSelected();
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
        String basePath = rootFolder.getPath() + File.separator + basePackage.replace(".", File.separator);
        MvpBuilder mvpBuilder = new MvpBuilder(rootFolder, basePath, basePackage, screenName, interfaces, shouldIncludeLibraryDependency);
        boolean success = mvpBuilder.build();

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
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        propertiesComponent.setValue(PROPERTY_PACKAGE_NAME, basePackage);
    }

    private void onCancel() {
        setVisible(false);
        dispose();
    }

    private boolean isMvpLibInstalled(boolean interfaces) {
        return GradleUtils.hasDependency(rootFolder, interfaces ? MVP_LIB_INTERFACES_DEPENDENCY_PKG : MVP_LIB_EVENTS_DEPENDENCY_PKG);
    }
}
