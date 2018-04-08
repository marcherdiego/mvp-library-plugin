package com.nerdscorner.mvp.events.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import static com.nerdscorner.mvp.events.utils.Constants.BUILD;
import static com.nerdscorner.mvp.events.utils.Constants.GENERATED;

public class SourceFolderSelectorDialog extends JDialog {
    private final Project project;
    private AnActionEvent actionEvent;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<VirtualFile> sourceFolders;

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
        DefaultListModel<VirtualFile> model = new DefaultListModel<>();
        VirtualFile[] sourceFolders = ProjectRootManager.getInstance(project).getContentSourceRoots();
        for (VirtualFile sourceFolder : sourceFolders) {
            if (sourceFolder.getUrl().contains(GENERATED) || sourceFolder.getUrl().contains(BUILD)) {
                continue;
            }
            model.addElement(sourceFolder);
        }
        this.sourceFolders.setModel(model);
        this.sourceFolders.setSelectedIndex(0);
    }

    private void onOK() {
        VirtualFile baseFolder = sourceFolders.getSelectedValue();

        PackageAndScreenInputDialog packageAndScreenInputDialog = new PackageAndScreenInputDialog(project, baseFolder, actionEvent);
        packageAndScreenInputDialog.pack();
        packageAndScreenInputDialog.setLocationRelativeTo(null);
        packageAndScreenInputDialog.setResizable(true);
        packageAndScreenInputDialog.setTitle("Enter your package name and screen name");
        packageAndScreenInputDialog.getButtonOK().requestFocusInWindow();
        packageAndScreenInputDialog.setVisible(true);

        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
