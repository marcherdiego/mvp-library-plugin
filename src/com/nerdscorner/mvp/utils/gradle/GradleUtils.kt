package com.nerdscorner.mvp.utils.gradle

import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.utils.FileReader

object GradleUtils {
    private const val LATEST_EVENTS_LIB_VERSION = "1.1.2"
    const val MVP_LIB_EVENTS_DEPENDENCY_PKG = "'com.github.marcherdiego.mvp:events:"
    const val MVP_LIB_EVENTS_DEPENDENCY = "$MVP_LIB_EVENTS_DEPENDENCY_PKG$LATEST_EVENTS_LIB_VERSION'"

    private const val LATEST_COROUTINES_LIB_VERSION = "1.1.2"
    const val COROUTINES_LIB_EVENTS_DEPENDENCY_PKG = "'com.github.marcherdiego.mvp:coroutines:"
    const val COROUTINES_LIB_EVENTS_DEPENDENCY = "$COROUTINES_LIB_EVENTS_DEPENDENCY_PKG$LATEST_COROUTINES_LIB_VERSION'"

    private const val GRADLE_FILE = "build.gradle"
    private const val ANDROID_SYNC_COMMAND = "Android.SyncProject"

    fun getGradleFileContent(rootFolder: VirtualFile): String? {
        try {
            val appGradleFile = getAppGradleFile(rootFolder)
            return FileReader.getFileContents(FileInputStream(appGradleFile!!.path))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getAppGradleFile(rootFolder: VirtualFile): VirtualFile? {
        return rootFolder
                .parent
                .parent
                .parent
                .findChild(GRADLE_FILE)
    }

    @JvmStatic
    fun hasDependency(root: VirtualFile, dependency: String) = getGradleFileContent(root)?.contains(dependency) ?: false

    fun restoreGradleFile(savedGradleFile: String?, rootFolder: VirtualFile) {
        if (savedGradleFile == null) {
            return
        }
        try {
            val manifestFile = File(getAppGradleFile(rootFolder)!!.path)
            val fileWriter = FileWriter(manifestFile)
            fileWriter.write(savedGradleFile)
            fileWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun performSync(actionEvent: AnActionEvent) {
        ActionManager
                .getInstance()
                .getAction(ANDROID_SYNC_COMMAND)
                .actionPerformed(actionEvent)
    }
}
