package com.nerdscorner.mvp.utils

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult

object GradleUtils {
    private const val LATEST_EVENTS_LIB_VERSION = "8.0.1"

    const val MVP_LIB_EVENTS_DEPENDENCY_PKG = "'com.nerdscorner.mvp:events:"
    const val MVP_LIB_EVENTS_DEPENDENCY = "$MVP_LIB_EVENTS_DEPENDENCY_PKG$LATEST_EVENTS_LIB_VERSION'"

    private const val IMPLEMENTATION = "\timplementation "
    private const val DEPENDENCIES_BLOCK_BEGIN = "dependencies {"
    private const val DEPENDENCIES_BLOCK_BEGIN_ALT = "dependencies{"

    private const val GRADLE_FILE = "build.gradle"
    private const val ANDROID_SYNC_COMMAND = "Android.SyncProject"

    //Reading states
    private const val SEEKING = 0
    private const val DONE = 2

    fun addDependency(rootFolder: VirtualFile, dependency: String): ExecutionResult {
        try {
            val appGradleFile = getAppGradleFile(rootFolder)
            val gradleFileContent = getGradleFileContent(rootFolder)
            var dependenciesBlockBegin = gradleFileContent?.indexOf(DEPENDENCIES_BLOCK_BEGIN)
            if (dependenciesBlockBegin == -1) {
                dependenciesBlockBegin = gradleFileContent?.indexOf(DEPENDENCIES_BLOCK_BEGIN_ALT)
                if (dependenciesBlockBegin == -1) {
                    throw IllegalStateException(
                            "Malformed build.gradle file, dependencies block not recognized. Expected dependencies { ... }"
                    )
                }
            }

            val reader = BufferedReader(InputStreamReader(ByteArrayInputStream(appGradleFile?.contentsToByteArray())))
            val gradleFileBuilder = StringBuilder()
            var state = SEEKING
            for (line in reader.lines()) {
                gradleFileBuilder
                        .append(line)
                        .append(System.lineSeparator())
                if (state == DONE) {
                    continue
                }
                if (line.contains(DEPENDENCIES_BLOCK_BEGIN) || line.contains(DEPENDENCIES_BLOCK_BEGIN_ALT)) {
                    gradleFileBuilder.append(IMPLEMENTATION).append(dependency).append(System.lineSeparator())
                    state = DONE
                }
            }
            val fileWriter = FileWriter(File(getAppGradleFile(rootFolder)?.path))
            fileWriter.write(gradleFileBuilder.toString())
            fileWriter.close()
            return ExecutionResult(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return ExecutionResult(false, e.message)
        }
    }

    fun getGradleFileContent(rootFolder: VirtualFile): String? {
        val appGradleFile = getAppGradleFile(rootFolder)
        try {
            return FileReader.getFileContents(FileInputStream(appGradleFile?.path))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getAppGradleFile(rootFolder: VirtualFile): VirtualFile? {
        return rootFolder
                .parent
                .parent
                .parent
                .findChild(GRADLE_FILE)
    }

    @JvmStatic
    fun hasDependency(rootFolder: VirtualFile, dependency: String): Boolean {
        return getGradleFileContent(rootFolder)?.contains(dependency) ?: false
    }

    fun restoreGradleFile(savedGradleFile: String?, rootFolder: VirtualFile) {
        if (savedGradleFile == null) {
            return
        }
        try {
            val manifestFile = File(getAppGradleFile(rootFolder)?.path)
            val fileWriter = FileWriter(manifestFile)
            fileWriter.write(savedGradleFile)
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun performSync(actionEvent: AnActionEvent) {
        val am = ActionManager.getInstance()
        val sync = am.getAction(ANDROID_SYNC_COMMAND)
        sync.actionPerformed(actionEvent)
    }
}
