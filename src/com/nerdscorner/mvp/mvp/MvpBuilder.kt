package com.nerdscorner.mvp.mvp

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult
import com.nerdscorner.mvp.utils.GradleUtils

abstract class MvpBuilder(private val shouldIncludeLibraryDependency: Boolean,
                          protected val isJava: Boolean,
                          protected val shouldCreateWiring: Boolean) {
    protected var savedGradleFile: String? = null

    protected var modelFileCreated = ExecutionResult.EMPTY
    protected var viewFileCreated = ExecutionResult.EMPTY
    protected var presenterFileCreated = ExecutionResult.EMPTY
    protected var layoutFileCreated = ExecutionResult.EMPTY

    abstract fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): ExecutionResult

    protected fun updateGradleFile(rootFolder: VirtualFile): ExecutionResult {
        return if (shouldIncludeLibraryDependency) {
            savedGradleFile = GradleUtils.getGradleFileContent(rootFolder)
            return GradleUtils.addDependency(rootFolder, GradleUtils.MVP_LIB_EVENTS_DEPENDENCY)
        } else {
            ExecutionResult(true)
        }
    }
}
