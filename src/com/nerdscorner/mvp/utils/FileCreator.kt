package com.nerdscorner.mvp.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object FileCreator {

    private const val PACKAGE_NAME_KEY = "\$PACKAGE_NAME$"
    private const val SCREEN_NAME_KEY = "\$SCREEN_NAME$"
    private const val LAYOUT_NAME_KEY = "\$LAYOUT_NAME$"

    @Throws(IOException::class)
    fun createFile(inputStream: InputStream, file: File, basePackage: String, screenName: String) {
        if (file.exists()) {
            throw IOException("File ${file.name} already exist")
        }
        val reader = BufferedReader(InputStreamReader(inputStream))
        val baseComponentContent = StringBuilder()
        for (line in reader.lines()) {
            baseComponentContent
                    .append(line)
                    .append(System.lineSeparator())
        }
        //Parameters replacer
        val parsedContent = baseComponentContent
                .toString()
                .replace(PACKAGE_NAME_KEY, basePackage)
                .replace(SCREEN_NAME_KEY, screenName)
                .replace(LAYOUT_NAME_KEY, StringUtils.replaceCamelCaseWithSnakeCase(screenName))

        file.parentFile.mkdirs()
        FileWriter(file).apply {
            write(parsedContent)
            close()
        }
    }
}
