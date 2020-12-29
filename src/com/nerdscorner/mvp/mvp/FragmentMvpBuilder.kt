package com.nerdscorner.mvp.mvp

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult
import com.nerdscorner.mvp.mvp.busevents.fragment.FragmentComponent
import com.nerdscorner.mvp.mvp.busevents.layout.LayoutComponent
import com.nerdscorner.mvp.mvp.busevents.model.ModelComponent
import com.nerdscorner.mvp.mvp.busevents.presenter.FragmentPresenterComponent
import com.nerdscorner.mvp.mvp.busevents.view.FragmentViewComponent
import com.nerdscorner.mvp.utils.GradleUtils

class FragmentMvpBuilder(shouldIncludeLibraryDependency: Boolean, isJava: Boolean, shouldCreateWiring: Boolean) :
        MvpBuilder(shouldIncludeLibraryDependency, isJava, shouldCreateWiring) {
    private var fragmentFileCreated = ExecutionResult.EMPTY

    override fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): ExecutionResult {
        val fragmentComponent = FragmentComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val modelComponent = ModelComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val viewComponent = FragmentViewComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val presenterComponent = FragmentPresenterComponent(fullPath, packageName, screenName, shouldCreateWiring)
        val layoutComponent = LayoutComponent(fullPath, packageName, screenName, false)

        fragmentFileCreated = fragmentComponent.build(isJava)
        modelFileCreated = modelComponent.build(isJava)
        viewFileCreated = viewComponent.build(isJava)
        presenterFileCreated = presenterComponent.build(isJava)
        layoutFileCreated = layoutComponent.build()

        val executionResult = fragmentFileCreated +
                              modelFileCreated +
                              viewFileCreated +
                              presenterFileCreated +
                              layoutFileCreated +
                              updateGradleFile(rootFolder)
        if (executionResult.successful.not()) {
            rollback(rootFolder, fragmentComponent, modelComponent, viewComponent, presenterComponent, layoutComponent)
        }
        return executionResult
    }

    private fun rollback(rootFolder: VirtualFile,
                         fragmentComponent: BaseComponent,
                         modelComponent: BaseComponent,
                         viewComponent: BaseComponent,
                         presenterComponent: BaseComponent,
                         layoutComponent: LayoutComponent) {
        if (fragmentFileCreated.successful) {
            fragmentComponent.rollback()
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
        GradleUtils.restoreGradleFile(savedGradleFile, rootFolder)
    }
}

