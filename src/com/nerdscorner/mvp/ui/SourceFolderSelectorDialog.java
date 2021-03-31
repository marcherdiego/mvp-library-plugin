package com.nerdscorner.mvp.ui;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.domain.VirtualFileWrapper;
import com.nerdscorner.mvp.utils.Constants;

public class SourceFolderSelectorDialog extends JDialog {
    private static final String PROPERTY_SOURCE_FOLDER_NAME = "source_folder_name";

    private final Project project;
    private AnActionEvent actionEvent;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<VirtualFileWrapper> sourceFolders;

    public SourceFolderSelectorDialog(Project project, AnActionEvent actionEvent) {
        this.project = project;
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
        loadSrcFolders();
    }

    private void loadSrcFolders() {
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        String sourceFolderName = propertiesComponent.getValue(PROPERTY_SOURCE_FOLDER_NAME, "");

        DefaultListModel<VirtualFileWrapper> model = new DefaultListModel<>();
        VirtualFile[] sourceFolders = ProjectRootManager.getInstance(project).getContentSourceRoots();
        int currentIndex = 0;
        int selectedIndex = 0;
        for (VirtualFile sourceFolder : sourceFolders) {
            if (sourceFolder.getUrl().contains(Constants.GENERATED) || sourceFolder.getUrl().contains(Constants.BUILD)) {
                continue;
            }
            if (sourceFolder.getPath().equals(sourceFolderName)) {
                selectedIndex = currentIndex;
            }
            model.addElement(new VirtualFileWrapper(project.getName(), sourceFolder));
            currentIndex++;
        }
        this.sourceFolders.setModel(model);
        this.sourceFolders.setSelectedIndex(selectedIndex);
    }

    private void onOK() {
        onCancel();

        VirtualFile baseFolder = sourceFolders.getSelectedValue().getVirtualFile();

        //Save state
        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance(project);
        propertiesComponent.setValue(PROPERTY_SOURCE_FOLDER_NAME, baseFolder.getPath());

        PackageAndScreenInputDialog packageAndScreenInputDialog = new PackageAndScreenInputDialog(project, baseFolder, actionEvent);
        packageAndScreenInputDialog.pack();
        packageAndScreenInputDialog.setLocationRelativeTo(null);
        packageAndScreenInputDialog.setResizable(true);
        packageAndScreenInputDialog.setTitle("Enter your package name and screen name");
        packageAndScreenInputDialog.getButtonOK().requestFocusInWindow();
        packageAndScreenInputDialog.setVisible(true);
    }

    private void onCancel() {
        setVisible(false);
        dispose();
    }
}
