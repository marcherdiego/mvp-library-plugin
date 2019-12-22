package com.nerdscorner.mvp.mvp


import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.mvp.busevents.activity.ActivityComponent
import com.nerdscorner.mvp.mvp.busevents.layout.LayoutComponent
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent
import com.nerdscorner.mvp.mvp.busevents.presenter.ActivityPresenterComponent
import com.nerdscorner.mvp.mvp.busevents.view.ActivityViewComponent
import com.nerdscorner.mvp.utils.GradleUtils
import com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY
import com.nerdscorner.mvp.utils.ManifestUtils

class ActivityMvpBuilder(shouldIncludeLibraryDependency: Boolean, isJava: Boolean, shouldCreateWiring: Boolean) : MvpBuilder(shouldIncludeLibraryDependency, isJava, shouldCreateWiring) {

    private var savedManifest: String? = null

    override fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): Boolean {
        val activityComponent = ActivityComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val modelComponent = ModelComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val viewComponent = ActivityViewComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val presenterComponent = ActivityPresenterComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val layoutComponent = LayoutComponent(fullPath, packageName, screenName, true)
        var success = activityComponent.build(isJava)
        success = (success && modelComponent.build(isJava)
                && viewComponent.build(isJava)
                && presenterComponent.build(isJava)
                && layoutComponent.build())
        success = updateManifestAndGradle(rootFolder, packageName, screenName, success)
        if (success.not()) {
            rollback(rootFolder, activityComponent, modelComponent, viewComponent, presenterComponent, layoutComponent, savedManifest)
        }
        return success
    }

    private fun updateManifestAndGradle(rootFolder: VirtualFile, packageName: String, screenName: String, success: Boolean): Boolean {
        var success = success
        savedManifest = ManifestUtils.getManifestString(rootFolder)
        success = success && ManifestUtils.addActivityToManifest(packageName, screenName, rootFolder)
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder)
            success = success && GradleUtils.addDependency(rootFolder, MVP_LIB_EVENTS_DEPENDENCY)
        }
        return success
    }

    private fun rollback(rootFolder: VirtualFile, activityComponent: BaseComponent, modelComponent: BaseComponent,
                         viewComponent: BaseComponent, presenterComponent: BaseComponent, layoutComponent: LayoutComponent,
                         savedManifest: String?) {
        activityComponent.rollback()
        modelComponent.rollback()
        viewComponent.rollback()
        presenterComponent.rollback()
        layoutComponent.rollback()
        ManifestUtils.restoreManifest(savedManifest, rootFolder.path)
        GradleUtils.restoreGradleFile(savedGradleFile, rootFolder)
    }
}
