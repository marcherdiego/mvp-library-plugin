package com.nerdscorner.mvp.domain

import com.intellij.openapi.vfs.VirtualFile

class VirtualFileWrapper(private val projectName: String, val virtualFile: VirtualFile) {
    override fun toString(): String {
        val fullPath = virtualFile.toString()
        return fullPath.substring(fullPath.indexOf(projectName))
    }
}
