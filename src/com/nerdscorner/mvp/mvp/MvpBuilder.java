package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.domain.manifest.Activity;
import com.nerdscorner.mvp.mvp.busevents.activity.ActivityComponent;
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent;
import com.nerdscorner.mvp.mvp.busevents.presenter.PresenterComponent;
import com.nerdscorner.mvp.mvp.busevents.view.ViewComponent;
import com.nerdscorner.mvp.utils.GradleUtils;
import com.nerdscorner.mvp.utils.ManifestUtils;

import org.jetbrains.annotations.Nullable;

import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY;
import static com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_INTERFACES_DEPENDENCY;

public class MvpBuilder {

    private final VirtualFile rootFolder;
    private final String fullPath;
    private final String packageName;
    private final String screenName;
    private final boolean interfaces;
    private final boolean shouldIncludeLibraryDependency;
    private Activity activityBaseClass;
    private @Nullable Activity modelBaseClass;
    private @Nullable Activity viewBaseClass;
    private @Nullable Activity presenterBaseClass;
    private String savedManifest;
    private String savedGradleFile;


    public MvpBuilder(VirtualFile rootFolder, String fullPath, String packageName, String screenName, boolean interfaces,
                      boolean shouldIncludeLibraryDependency) {
        this.rootFolder = rootFolder;
        this.fullPath = fullPath;
        this.packageName = packageName;
        this.screenName = screenName;
        this.interfaces = interfaces;
        this.shouldIncludeLibraryDependency = shouldIncludeLibraryDependency;
    }

    public boolean build() {
        if (interfaces) {
            //IS INTERFACES-LINK COMMUNICATION
            com.nerdscorner.mvp.mvp.interfaces.activity.ActivityComponent activityComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.activity.ActivityComponent(fullPath, packageName, screenName);
            com.nerdscorner.mvp.mvp.interfaces.model.ModelComponent modelComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.model.ModelComponent(fullPath, packageName, screenName);
            com.nerdscorner.mvp.mvp.interfaces.view.ViewComponent viewComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.view.ViewComponent(fullPath, packageName, screenName);
            com.nerdscorner.mvp.mvp.interfaces.presenter.PresenterComponent presenterComponent =
                    new com.nerdscorner.mvp.mvp.interfaces.presenter.PresenterComponent(fullPath, packageName, screenName);
            boolean success =
                    activityComponent.build()
                            && modelComponent.build()
                            && viewComponent.build()
                            && presenterComponent.build();
            success = updateManifestAndGradle(success);
            checkSuccessOrRollback(success, activityComponent, modelComponent, viewComponent, presenterComponent, savedManifest);
            return success;
        } else {
            ActivityComponent activityComponent = new ActivityComponent(fullPath, packageName, screenName);
            ModelComponent modelComponent = new ModelComponent(fullPath, packageName, screenName);
            ViewComponent viewComponent = new ViewComponent(fullPath, packageName, screenName);
            PresenterComponent presenterComponent = new PresenterComponent(fullPath, packageName, screenName);
            boolean success =
                    activityComponent.build()
                            && modelComponent.build()
                            && viewComponent.build()
                            && presenterComponent.build();
            success = updateManifestAndGradle(success);
            checkSuccessOrRollback(success, activityComponent, modelComponent, viewComponent, presenterComponent, savedManifest);
            return success;
        }
    }

    private boolean updateManifestAndGradle(boolean success) {
        savedManifest = ManifestUtils.getManifestString(rootFolder);
        success = success && ManifestUtils.addActivityToManifest(packageName, screenName, rootFolder);
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder);
            success = success && GradleUtils.addDependency(rootFolder, interfaces ? MVP_LIB_INTERFACES_DEPENDENCY : MVP_LIB_EVENTS_DEPENDENCY);
        }
        return success;
    }

    private void checkSuccessOrRollback(boolean success, BaseComponent activityComponent, BaseComponent modelComponent, BaseComponent viewComponent, BaseComponent presenterComponent, String savedManifest) {
        if (!success) {
            activityComponent.rollback();
            modelComponent.rollback();
            viewComponent.rollback();
            presenterComponent.rollback();
            ManifestUtils.restoreManifest(savedManifest, rootFolder.getPath());
            GradleUtils.restoreGradleFile(savedGradleFile, rootFolder);
        }
    }

    public void setBaseClasses(Activity activityBaseClass, Activity modelBaseClass, Activity viewBaseClass, Activity presenterBaseClass) {
        this.activityBaseClass = activityBaseClass;
        this.modelBaseClass = modelBaseClass;
        this.viewBaseClass = viewBaseClass;
        this.presenterBaseClass = presenterBaseClass;
    }
}
