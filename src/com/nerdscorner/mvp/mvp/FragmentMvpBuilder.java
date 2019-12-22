package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.mvp.busevents.fragment.FragmentComponent;
import com.nerdscorner.mvp.mvp.busevents.layout.LayoutComponent;
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent;
import com.nerdscorner.mvp.mvp.busevents.presenter.FragmentPresenterComponent;
import com.nerdscorner.mvp.mvp.busevents.view.FragmentViewComponent;
import com.nerdscorner.mvp.utils.GradleUtils;


import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY;

public class FragmentMvpBuilder extends MvpBuilder {

    public FragmentMvpBuilder(boolean shouldIncludeLibraryDependency, boolean isJava, boolean shouldCreateWiring) {
        super(shouldIncludeLibraryDependency, isJava, shouldCreateWiring);
    }

    @Override
    public boolean build(VirtualFile rootFolder, String fullPath, String packageName, String screenName) {
        FragmentComponent fragmentComponent = new FragmentComponent(fullPath, packageName, screenName, shouldCreateWiring);
        ModelComponent modelComponent = new ModelComponent(fullPath, packageName, screenName, shouldCreateWiring);
        FragmentViewComponent fragmentViewComponent = new FragmentViewComponent(fullPath, packageName, screenName, shouldCreateWiring);
        FragmentPresenterComponent fragmentPresenterComponent = new FragmentPresenterComponent(fullPath, packageName, screenName, shouldCreateWiring);
        LayoutComponent layoutComponent = new LayoutComponent(fullPath, packageName, screenName, false);
        boolean success = fragmentComponent.build(isJava);
        success = success && modelComponent.build(isJava)
                && fragmentViewComponent.build(isJava)
                && fragmentPresenterComponent.build(isJava)
                && layoutComponent.build();
        success = updateGradle(rootFolder, success);
        if (!success) {
            rollback(rootFolder, fragmentComponent, modelComponent, fragmentViewComponent, fragmentPresenterComponent, layoutComponent);
        }
        return success;
    }

    private boolean updateGradle(VirtualFile rootFolder, boolean success) {
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder);
            success = success && GradleUtils.addDependency(rootFolder, MVP_LIB_EVENTS_DEPENDENCY);
        }
        return success;
    }

    private void rollback(VirtualFile rootFolder, BaseComponent activityComponent,
                          BaseComponent modelComponent, BaseComponent viewComponent, BaseComponent presenterComponent,
                          LayoutComponent layoutComponent) {
        activityComponent.rollback();
        modelComponent.rollback();
        viewComponent.rollback();
        presenterComponent.rollback();
        layoutComponent.rollback();
        GradleUtils.restoreGradleFile(savedGradleFile, rootFolder);
    }
}

