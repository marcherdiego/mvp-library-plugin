package com.nerdscorner.mvp.utils

import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader

import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException

import com.intellij.openapi.vfs.VirtualFile
import com.nerdscorner.mvp.domain.ExecutionResult
import com.nerdscorner.mvp.domain.manifest.Manifest
import com.nerdscorner.mvp.mvp.busevents.activity.ActivityComponent

object ManifestUtils {
    private const val XML_ACTIVITY_START_TAG = "<application"
    private const val XML_CLOSE_TAG = ">"

    private const val PACKAGE_NAME = "\$PACKAGE_NAME$"
    private const val ACTIVITY_NAME = "\$ACTIVITY_NAME$"
    private const val NEW_LINE = "\$NEW_LINE$"

    private const val UP_FOLDER_PATH = "/../"
    private const val MANIFEST_FILE_NAME = "AndroidManifest.xml"

    private const val MANIFEST_ACTIVITY_TEMPLATE = "\t\t<activity android:name=\"\$PACKAGE_NAME$.\$ACTIVITY_NAME$\">\$NEW_LINE$\t\t</activity>"

    // Reading states
    private const val SEEKING = 0
    private const val WAITING_FOR_ACTIVITY_CLOSE = 1
    private const val DONE = 2

    @JvmStatic
    fun getManifest(sourceFolder: VirtualFile): Manifest? {
        try {
            sourceFolder.refresh(false, true)
            val jaxbContext = JAXBContext.newInstance(Manifest::class.java)
            val jaxbUnmarshaller = jaxbContext.createUnmarshaller()
            val manifestFile = sourceFolder.parent.findChild(MANIFEST_FILE_NAME)
            return jaxbUnmarshaller.unmarshal(ByteArrayInputStream(manifestFile?.contentsToByteArray())) as Manifest
        } catch (e: JAXBException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getManifestString(sourceFolder: VirtualFile): String? {
        try {
            sourceFolder.refresh(false, true)
            val manifestFile = sourceFolder.parent.findChild(MANIFEST_FILE_NAME)
            val reader = BufferedReader(InputStreamReader(ByteArrayInputStream(manifestFile?.contentsToByteArray())))
            val manifestFileBuilder = StringBuilder()
            for (line in reader.lines()) {
                manifestFileBuilder
                        .append(line)
                        .append(System.lineSeparator())
            }
            return manifestFileBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun addActivityToManifest(pkgName: String, screenName: String, sourceFolder: VirtualFile): ExecutionResult {
        val packageName = "$pkgName${ActivityComponent.PACKAGE_SUFFIX}"
        val activityName = "$screenName${ActivityComponent.ACTIVITY_SUFFIX}"
        try {
            sourceFolder.refresh(false, true)
            val manifestFile = sourceFolder.parent.findChild(MANIFEST_FILE_NAME)
            val reader = BufferedReader(InputStreamReader(ByteArrayInputStream(manifestFile?.contentsToByteArray())))
            val manifestFileBuilder = StringBuilder()
            var state = SEEKING
            for (line in reader.lines()) {
                manifestFileBuilder
                        .append(line)
                        .append(System.lineSeparator())
                if (state == DONE) {
                    continue
                }
                if (line.contains(XML_ACTIVITY_START_TAG)) {
                    state = if (line.contains(XML_CLOSE_TAG)) {
                        appendActivity(packageName, activityName, manifestFileBuilder)
                    } else {
                        WAITING_FOR_ACTIVITY_CLOSE
                    }
                } else {
                    if (state == WAITING_FOR_ACTIVITY_CLOSE && line.contains(XML_CLOSE_TAG)) {
                        state = appendActivity(packageName, activityName, manifestFileBuilder)
                    }
                }
            }
            val fileWriter = FileWriter(File(sourceFolder.path + UP_FOLDER_PATH + MANIFEST_FILE_NAME))
            fileWriter.write(manifestFileBuilder.toString())
            fileWriter.close()
            return ExecutionResult(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return ExecutionResult(false, e.message)
        }
    }

    private fun appendActivity(packageName: String, activityName: String, manifestFileBuilder: StringBuilder): Int {
        manifestFileBuilder.append(
                MANIFEST_ACTIVITY_TEMPLATE
                        .replace(PACKAGE_NAME, packageName)
                        .replace(ACTIVITY_NAME, activityName)
                        .replace(NEW_LINE, System.lineSeparator())
        ).append(System.lineSeparator())
        return DONE
    }

    fun restoreManifest(savedManifest: String?, projectRoot: String) {
        if (savedManifest == null) {
            return
        }
        try {
            val manifestFile = File(projectRoot + UP_FOLDER_PATH + MANIFEST_FILE_NAME)
            val fileWriter = FileWriter(manifestFile)
            fileWriter.write(savedManifest)
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
