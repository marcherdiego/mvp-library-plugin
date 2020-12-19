package com.nerdscorner.mvp.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

internal object FileReader {

    @Throws(IOException::class)
    fun getFileContents(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val rawFileContent = StringBuilder()
        for (line in reader.lines()) {
            rawFileContent
                    .append(line)
                    .append(System.lineSeparator())
        }
        return rawFileContent.toString()
    }
}
