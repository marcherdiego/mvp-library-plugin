package com.nerdscorner.mvp.mvp

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult
import com.nerdscorner.mvp.utils.gradle.DependenciesManager
import com.nerdscorner.mvp.utils.gradle.GradleUtils
import com.nerdscorner.mvp.utils.ifAny

abstract class MvpBuilder(private val shouldIncludeLibraryDependency: Boolean,
                          private val shouldIncludeCoroutinesLibraryDependency: Boolean,
                          protected val isJava: Boolean,
                          protected val shouldCreateWiring: Boolean) {
    protected var savedGradleFile: String? = null

    protected var modelFileCreated = ExecutionResult.EMPTY
    protected var viewFileCreated = ExecutionResult.EMPTY
    protected var presenterFileCreated = ExecutionResult.EMPTY
    protected var layoutFileCreated = ExecutionResult.EMPTY

    abstract fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): ExecutionResult

    protected fun updateGradleFile(rootFolder: VirtualFile): ExecutionResult {
        var executionResult = ExecutionResult(true)
        ifAny(shouldIncludeLibraryDependency, shouldIncludeCoroutinesLibraryDependency) { includeMainLib, includeCoroutines ->
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder)
            val dependenciesManager = DependenciesManager(rootFolder)
            if (includeMainLib) {
                dependenciesManager.addDependency(GradleUtils.MVP_LIB_EVENTS_DEPENDENCY)
            }
            if (includeCoroutines) {
                dependenciesManager.addDependency(GradleUtils.COROUTINES_LIB_EVENTS_DEPENDENCY)
            }
            executionResult += dependenciesManager.saveGradleFile()
        }
        return executionResult
    }
}
