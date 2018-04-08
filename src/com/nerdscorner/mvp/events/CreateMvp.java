package com.nerdscorner.mvp.events;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.nerdscorner.mvp.events.ui.PackageAndScreenInputDialog;
import com.nerdscorner.mvp.events.ui.SourceFolderSelectorDialog;

public class CreateMvp extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        Project project = getEventProject(actionEvent);
        if (project == null) {
            return;
        }

        ProjectRootManager projectRootManager = ProjectRootManager.getInstance(project);
        if (projectRootManager.getContentSourceRoots().length > 1) {
            SourceFolderSelectorDialog sourceFolderSelectorDialog = new SourceFolderSelectorDialog(project, actionEvent);
            sourceFolderSelectorDialog.pack();
            sourceFolderSelectorDialog.setLocationRelativeTo(null);
            sourceFolderSelectorDialog.setTitle("Select source folder");
            sourceFolderSelectorDialog.setResizable(true);
            sourceFolderSelectorDialog.setVisible(true);
        } else {
            PackageAndScreenInputDialog packageAndScreenInputDialog = new PackageAndScreenInputDialog(
                    project,
                    projectRootManager
                            .getContentSourceRoots()[0],
                    actionEvent
            );
            packageAndScreenInputDialog.pack();
            packageAndScreenInputDialog.setLocationRelativeTo(null);
            packageAndScreenInputDialog.setResizable(true);
            packageAndScreenInputDialog.setTitle("Enter your package and screen name");
            packageAndScreenInputDialog.getButtonOK().requestFocusInWindow();
            packageAndScreenInputDialog.setVisible(true);
        }
    }

    @Override
    public void update(final AnActionEvent actionEvent) {
        final Project project = actionEvent.getData(CommonDataKeys.PROJECT);
        actionEvent.getPresentation().setVisible(project != null);
    }
}
