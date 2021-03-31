package com.nerdscorner.mvp.utils.gradle

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

class DependenciesManager(rootFolder: VirtualFile) {
    private var gradleFile = GradleUtils.getAppGradleFile(rootFolder)!!
    private var gradleFileReader: BufferedReader

    private val dependencies = mutableListOf<String>()

    init {
        val gradleFileContent = GradleUtils.getGradleFileContent(rootFolder)
        var dependenciesBlockBegin = gradleFileContent?.indexOf(DEPENDENCIES_BLOCK_BEGIN)
        if (dependenciesBlockBegin == -1) {
            dependenciesBlockBegin = gradleFileContent?.indexOf(DEPENDENCIES_BLOCK_BEGIN_ALT)
            if (dependenciesBlockBegin == -1) {
                throw IllegalStateException(
                        "Malformed build.gradle file, dependencies block not recognized. Expected dependencies { ... }"
                )
            }
        }

        gradleFileReader = BufferedReader(InputStreamReader(ByteArrayInputStream(gradleFile.contentsToByteArray())))
    }

    fun addDependency(dependency: String) {
        dependencies.add(dependency)
    }

    fun saveGradleFile(): ExecutionResult {
        try {
            val gradleFileBuilder = StringBuilder()
            var done = false
            for (line in gradleFileReader.lines()) {
                gradleFileBuilder
                        .append(line)
                        .append(System.lineSeparator())
                if (done) {
                    continue
                }
                if (line.contains(DEPENDENCIES_BLOCK_BEGIN) || line.contains(DEPENDENCIES_BLOCK_BEGIN_ALT)) {
                    dependencies.forEach { dependency ->
                        gradleFileBuilder
                                .append(IMPLEMENTATION)
                                .append(dependency)
                                .append(System.lineSeparator())
                    }
                    done = true
                }
            }
            with(FileWriter(File(gradleFile.path))) {
                write(gradleFileBuilder.toString())
                flush()
                close()
            }
            return ExecutionResult(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return ExecutionResult(false, e.message)
        }
    }

    companion object {
        private const val IMPLEMENTATION = "\timplementation "
        private const val DEPENDENCIES_BLOCK_BEGIN = "dependencies {"
        private const val DEPENDENCIES_BLOCK_BEGIN_ALT = "dependencies{"
    }
}
