package com.nerdscorner.mvp.domain

import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class VirtualFileWrapper(private val projectPath: String?, val virtualFile: VirtualFile) {
    override fun toString(): String {
        val fullPath = virtualFile.toString()
        return try {
            val projectNameIndex = fullPath.indexOf(projectPath!!)
            val pathUntilProjectNameLength = projectPath.length - projectPath.lastIndexOf(File.pathSeparatorChar)
            fullPath.substring(projectNameIndex + pathUntilProjectNameLength)
        } catch (e: Exception) {
            fullPath
        }
    }
}
