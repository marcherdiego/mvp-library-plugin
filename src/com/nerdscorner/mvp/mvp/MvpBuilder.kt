package com.nerdscorner.mvp.mvp

import com.intellij.openapi.vfs.VirtualFile

abstract class MvpBuilder(protected val shouldIncludeLibraryDependency: Boolean, protected val isJava: Boolean,
                          protected val shouldCreateWiring: Boolean) {
    protected var savedGradleFile: String? = null

    abstract fun build(rootFolder: VirtualFile, fullPath: String, packageName: String, screenName: String): Boolean
}
