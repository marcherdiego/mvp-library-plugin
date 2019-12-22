package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.mvp.busevents.activity.ActivityComponent;
import com.nerdscorner.mvp.mvp.busevents.layout.LayoutComponent;
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent;
import com.nerdscorner.mvp.mvp.busevents.presenter.ActivityPresenterComponent;
import com.nerdscorner.mvp.mvp.busevents.view.ActivityViewComponent;
import com.nerdscorner.mvp.utils.GradleUtils;
import com.nerdscorner.mvp.utils.ManifestUtils;


import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY;

public class ActivityMvpBuilder extends MvpBuilder {

    private String savedManifest;

    public ActivityMvpBuilder(boolean shouldIncludeLibraryDependency, boolean isJava, boolean shouldCreateWiring) {
        super(shouldIncludeLibraryDependency, isJava, shouldCreateWiring);
    }

    @Override
    public boolean build(VirtualFile rootFolder, String fullPath, String packageName, String screenName) {
        ActivityComponent activityComponent = new ActivityComponent(fullPath, packageName, screenName, shouldCreateWiring);
        ModelComponent modelComponent = new ModelComponent(fullPath, packageName, screenName, shouldCreateWiring);
        ActivityViewComponent viewComponent = new ActivityViewComponent(fullPath, packageName, screenName, shouldCreateWiring);
        ActivityPresenterComponent presenterComponent = new ActivityPresenterComponent(fullPath, packageName, screenName, shouldCreateWiring);
        LayoutComponent layoutComponent = new LayoutComponent(fullPath, packageName, screenName, true);
        boolean success = activityComponent.build(isJava);
        success = success && modelComponent.build(isJava)
                && viewComponent.build(isJava)
                && presenterComponent.build(isJava)
                && layoutComponent.build();
        success = updateManifestAndGradle(rootFolder, packageName, screenName, success);
        if (!success) {
            rollback(rootFolder, activityComponent, modelComponent, viewComponent, presenterComponent, layoutComponent, savedManifest);
        }
        return success;
    }

    private boolean updateManifestAndGradle(VirtualFile rootFolder, String packageName, String screenName, boolean success) {
        savedManifest = ManifestUtils.getManifestString(rootFolder);
        success = success && ManifestUtils.addActivityToManifest(packageName, screenName, rootFolder);
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder);
            success = success && GradleUtils.addDependency(rootFolder, MVP_LIB_EVENTS_DEPENDENCY);
        }
        return success;
    }

    private void rollback(VirtualFile rootFolder, BaseComponent activityComponent, BaseComponent modelComponent,
                          BaseComponent viewComponent, BaseComponent presenterComponent, LayoutComponent layoutComponent,
                          String savedManifest) {
        activityComponent.rollback();
        modelComponent.rollback();
        viewComponent.rollback();
        presenterComponent.rollback();
        layoutComponent.rollback();
        ManifestUtils.restoreManifest(savedManifest, rootFolder.getPath());
        GradleUtils.restoreGradleFile(savedGradleFile, rootFolder);
    }
}
