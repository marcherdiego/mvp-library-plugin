package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.mvp.busevents.fragment.FragmentComponent;
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent;
import com.nerdscorner.mvp.mvp.busevents.presenter.FragmentPresenterComponent;
import com.nerdscorner.mvp.mvp.busevents.view.FragmentViewComponent;
import com.nerdscorner.mvp.utils.GradleUtils;

import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY;
import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_INTERFACES_DEPENDENCY;

public class FragmentMvpBuilder extends MvpBuilder {

    public FragmentMvpBuilder(boolean shouldIncludeLibraryDependency, boolean isJava) {
        super(shouldIncludeLibraryDependency, isJava);
    }

    @Override
    public boolean build(VirtualFile rootFolder, String fullPath, String packageName, String screenName, boolean interfaces) {
        if (interfaces) {
            //IS INTERFACES-LINK COMMUNICATION
            com.nerdscorner.mvp.mvp.interfaces.fragment.FragmentComponent fragmentComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.fragment.FragmentComponent(fullPath, packageName, screenName);
            com.nerdscorner.mvp.mvp.interfaces.model.ModelComponent modelComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.model.ModelComponent(fullPath, packageName, screenName);
            com.nerdscorner.mvp.mvp.interfaces.view.FragmentViewComponent fragmentViewComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.view.FragmentViewComponent(fullPath, packageName, screenName);
            com.nerdscorner.mvp.mvp.interfaces.presenter.FragmentPresenterComponent fragmentPresenterComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.presenter.FragmentPresenterComponent(fullPath, packageName, screenName);
            boolean success =
                    fragmentComponent.build(isJava)
                            && modelComponent.build(isJava)
                            && fragmentViewComponent.build(isJava)
                            && fragmentPresenterComponent.build(isJava);
            checkSuccessOrRollback(rootFolder, success, fragmentComponent, modelComponent, fragmentViewComponent, fragmentPresenterComponent);
            return success;
        } else {
            FragmentComponent fragmentComponent = new FragmentComponent(fullPath, packageName, screenName);
            ModelComponent modelComponent = new ModelComponent(fullPath, packageName, screenName);
            FragmentViewComponent fragmentViewComponent = new FragmentViewComponent(fullPath, packageName, screenName);
            FragmentPresenterComponent fragmentPresenterComponent = new FragmentPresenterComponent(fullPath, packageName, screenName);
            boolean success =
                    fragmentComponent.build(isJava)
                            && modelComponent.build(isJava)
                            && fragmentViewComponent.build(isJava)
                            && fragmentPresenterComponent.build(isJava);
            success = updateGradle(rootFolder, interfaces, success);
            checkSuccessOrRollback(rootFolder, success, fragmentComponent, modelComponent, fragmentViewComponent, fragmentPresenterComponent);
            return success;
        }
    }

    private boolean updateGradle(VirtualFile rootFolder, boolean interfaces, boolean success) {
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder);
            success = success && GradleUtils.addDependency(rootFolder, interfaces ? MVP_LIB_INTERFACES_DEPENDENCY : MVP_LIB_EVENTS_DEPENDENCY);
        }
        return success;
    }

    private void checkSuccessOrRollback(VirtualFile rootFolder, boolean success, BaseComponent activityComponent,
                                        BaseComponent modelComponent, BaseComponent viewComponent, BaseComponent presenterComponent) {
        if (!success) {
            activityComponent.rollback();
            modelComponent.rollback();
            viewComponent.rollback();
            presenterComponent.rollback();
            GradleUtils.restoreGradleFile(savedGradleFile, rootFolder);
        }
    }
}

