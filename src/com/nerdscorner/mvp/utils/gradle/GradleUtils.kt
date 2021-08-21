package com.nerdscorner.mvp.utils.gradle

import com.intellij.ide.util.PropertiesComponent
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.utils.Constants
import com.nerdscorner.mvp.utils.FileReader

object GradleUtils {
    private val propertiesComponent = PropertiesComponent.getInstance()
    private var latestEventsLibVersion = propertiesComponent.getValue(Constants.Properties.PROPERTY_LATEST_EVENTS_LIB_VESION, "1.2.0")
    const val MVP_LIB_EVENTS_DEPENDENCY_PKG = "'com.github.marcherdiego.mvp:events:"

    private var latestCoroutinesLibVersion = propertiesComponent.getValue(Constants.Properties.PROPERTY_LATEST_COROUTINES_LIB_VESION, "1.2.0")
    const val COROUTINES_LIB_EVENTS_DEPENDENCY_PKG = "'com.github.marcherdiego.mvp:coroutines:"

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
            val manifestFile = File(getAppGradleFile(rootFolder)?.path ?: return)
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

    fun setLatestEventsLibVersion(version: String?) {
        latestEventsLibVersion = version ?: return
        propertiesComponent.setValue(Constants.Properties.PROPERTY_LATEST_EVENTS_LIB_VESION, latestEventsLibVersion)
    }

    fun setLatestCoroutinesLibVersion(version: String?) {
        latestCoroutinesLibVersion = version ?: return
        propertiesComponent.setValue(Constants.Properties.PROPERTY_LATEST_COROUTINES_LIB_VESION, latestCoroutinesLibVersion)
    }

    fun getLatestEventsLibVersion() = "$MVP_LIB_EVENTS_DEPENDENCY_PKG$latestEventsLibVersion'"

    fun getLatestCoroutinesLibVersion() = "$COROUTINES_LIB_EVENTS_DEPENDENCY_PKG$latestCoroutinesLibVersion'"
}
