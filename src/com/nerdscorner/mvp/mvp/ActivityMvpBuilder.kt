package com.nerdscorner.mvp.mvp

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult
import com.nerdscorner.mvp.mvp.busevents.activity.ActivityComponent
import com.nerdscorner.mvp.mvp.busevents.layout.LayoutComponent
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent
import com.nerdscorner.mvp.mvp.busevents.presenter.ActivityPresenterComponent
import com.nerdscorner.mvp.mvp.busevents.view.ActivityViewComponent
import com.nerdscorner.mvp.utils.GradleUtils
import com.nerdscorner.mvp.utils.ManifestUtils

class ActivityMvpBuilder(shouldIncludeLibraryDependency: Boolean, isJava: Boolean, shouldCreateWiring: Boolean) :
        MvpBuilder(shouldIncludeLibraryDependency, isJava, shouldCreateWiring) {

    private var savedManifest: String? = null
    private var activityFileCreated = ExecutionResult.EMPTY

    override fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): ExecutionResult {
        val activityComponent = ActivityComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val modelComponent = ModelComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val viewComponent = ActivityViewComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val presenterComponent = ActivityPresenterComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val layoutComponent = LayoutComponent(fullPath, packageName, screenName, true)

        activityFileCreated = activityComponent.build(isJava)
        modelFileCreated = modelComponent.build(isJava)
        viewFileCreated = viewComponent.build(isJava)
        presenterFileCreated = presenterComponent.build(isJava)
        layoutFileCreated = layoutComponent.build()

        val executionResult = activityFileCreated +
                              modelFileCreated +
                              viewFileCreated +
                              presenterFileCreated +
                              layoutFileCreated +
                              updateGradleFile(rootFolder) +
                              updateManifestFile(rootFolder, packageName, screenName)
        if (executionResult.successful.not()) {
            rollback(rootFolder, activityComponent, modelComponent, viewComponent, presenterComponent, layoutComponent)
        }
        return executionResult
    }

    private fun updateManifestFile(rootFolder: VirtualFile, packageName: String, screenName: String): ExecutionResult {
        savedManifest = ManifestUtils.getManifestString(rootFolder)
        return ManifestUtils.addActivityToManifest(packageName, screenName, rootFolder)
    }

    private fun rollback(rootFolder: VirtualFile, activityComponent: BaseComponent, modelComponent: BaseComponent,
                         viewComponent: BaseComponent, presenterComponent: BaseComponent, layoutComponent: LayoutComponent) {
        if (activityFileCreated.successful) {
            activityComponent.rollback()
        }
        if (modelFileCreated.successful) {
            modelComponent.rollback()
        }
        if (viewFileCreated.successful) {
            viewComponent.rollback()
        }
        if (presenterFileCreated.successful) {
            presenterComponent.rollback()
        }
        if (layoutFileCreated.successful) {
            layoutComponent.rollback()
        }
        ManifestUtils.restoreManifest(savedManifest, rootFolder.path)
        GradleUtils.restoreGradleFile(savedGradleFile, rootFolder)
    }
}
