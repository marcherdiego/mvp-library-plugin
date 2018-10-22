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

    public FragmentMvpBuilder(VirtualFile rootFolder, String fullPath, String packageName, String screenName, boolean interfaces,
                              boolean shouldIncludeLibraryDependency) {
        super(rootFolder, fullPath, packageName, screenName, interfaces, shouldIncludeLibraryDependency);
    }

    @Override
    public boolean build() {
        if (interfaces) {
            //IS INTERFACES-LINK COMMUNICATION
            //TODO
            return false;
        } else {
            FragmentComponent fragmentComponent = new FragmentComponent(fullPath, packageName, screenName);
            ModelComponent modelComponent = new ModelComponent(fullPath, packageName, screenName);
            FragmentViewComponent fragmentViewComponent = new FragmentViewComponent(fullPath, packageName, screenName);
            FragmentPresenterComponent fragmentPresenterComponent = new FragmentPresenterComponent(fullPath, packageName, screenName);
            boolean success =
                    fragmentComponent.build()
                            && modelComponent.build()
                            && fragmentViewComponent.build()
                            && fragmentPresenterComponent.build();
            success = updateGradle(success);
            checkSuccessOrRollback(success, fragmentComponent, modelComponent, fragmentViewComponent, fragmentPresenterComponent);
            return success;
        }
    }

    private boolean updateGradle(boolean success) {
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder);
            success = success && GradleUtils.addDependency(rootFolder, interfaces ? MVP_LIB_INTERFACES_DEPENDENCY : MVP_LIB_EVENTS_DEPENDENCY);
        }
        return success;
    }

    private void checkSuccessOrRollback(boolean success, BaseComponent activityComponent, BaseComponent modelComponent,
                                        BaseComponent viewComponent, BaseComponent presenterComponent) {
        if (!success) {
            activityComponent.rollback();
            modelComponent.rollback();
            viewComponent.rollback();
            presenterComponent.rollback();
            GradleUtils.restoreGradleFile(savedGradleFile, rootFolder);
        }
    }
}

