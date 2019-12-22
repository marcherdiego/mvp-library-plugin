package com.nerdscorner.mvp.mvp

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.mvp.busevents.fragment.FragmentComponent
import com.nerdscorner.mvp.mvp.busevents.layout.LayoutComponent
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent
import com.nerdscorner.mvp.mvp.busevents.presenter.FragmentPresenterComponent
import com.nerdscorner.mvp.mvp.busevents.view.FragmentViewComponent
import com.nerdscorner.mvp.utils.GradleUtils


import com.nerdscorner.mvp.utils.GradleUtils.MVP_LIB_EVENTS_DEPENDENCY

class FragmentMvpBuilder(shouldIncludeLibraryDependency: Boolean, isJava: Boolean, shouldCreateWiring: Boolean) : MvpBuilder(shouldIncludeLibraryDependency, isJava, shouldCreateWiring) {

    override fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): Boolean {
        val fragmentComponent = FragmentComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val modelComponent = ModelComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val fragmentViewComponent = FragmentViewComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val fragmentPresenterComponent = FragmentPresenterComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val layoutComponent = LayoutComponent(fullPath, packageName, screenName, false)
        var success = fragmentComponent.build(isJava)
        success = (success && modelComponent.build(isJava)
                && fragmentViewComponent.build(isJava)
                && fragmentPresenterComponent.build(isJava)
                && layoutComponent.build())
        success = updateGradle(rootFolder, success)
        if (success.not()) {
            rollback(rootFolder, fragmentComponent, modelComponent, fragmentViewComponent, fragmentPresenterComponent, layoutComponent)
        }
        return success
    }

    private fun updateGradle(rootFolder: VirtualFile, success: Boolean): Boolean {
        var success = success
        if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder)
            success = success && GradleUtils.addDependency(rootFolder, MVP_LIB_EVENTS_DEPENDENCY)
        }
        return success
    }

    private fun rollback(rootFolder: VirtualFile, activityComponent: BaseComponent,
                         modelComponent: BaseComponent, viewComponent: BaseComponent, presenterComponent: BaseComponent,
                         layoutComponent: LayoutComponent) {
        activityComponent.rollback()
        modelComponent.rollback()
        viewComponent.rollback()
        presenterComponent.rollback()
        layoutComponent.rollback()
        GradleUtils.restoreGradleFile(savedGradleFile, rootFolder)
    }
}

