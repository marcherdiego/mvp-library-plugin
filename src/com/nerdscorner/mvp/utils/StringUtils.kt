package com.nerdscorner.mvp.utils

import com.intellij.codeInsight.template.impl.TemplateSettings.SPACE_CHAR

object StringUtils {

    private const val HYPHEN = "_"

    @JvmStatic
    fun isEmpty(text: String?): Boolean {
        return text == null || text.isEmpty()
    }

    @JvmStatic
    fun asCamelCase(text: String?): String? {
        var text = text
        text = replaceSpacesWithCamelCase(text)
        return if (text.isNullOrEmpty()) {
            text
        } else {
            text.substring(0, 1).toUpperCase() + text.substring(1)
        }
    }

    private fun replaceSpacesWithCamelCase(text: String?): String? {
        if (text.isNullOrEmpty()) {
            return text
        }
        val stringBuilder = StringBuilder()
        var shouldCapitalizeLetter = false
        for (i in 0 until text.length) {
            val letter = text[i]
            when {
                letter == SPACE_CHAR -> shouldCapitalizeLetter = true
                shouldCapitalizeLetter -> {
                    shouldCapitalizeLetter = false
                    stringBuilder.append(letter.toString().toUpperCase())
                }
                else -> stringBuilder.append(letter)
            }
        }
        return stringBuilder.toString()
    }

    fun replaceCamelCaseWithSnakeCase(text: String): String {
        if (isEmpty(text)) {
            return text
        }
        val stringBuilder = StringBuilder()
        for (i in 0 until text.length) {
            val letter = text[i]
            if (Character.isUpperCase(letter)) {
                if (i > 0) {
                    stringBuilder.append(HYPHEN)
                }
                stringBuilder.append(letter.toString().toLowerCase())
            } else {
                stringBuilder.append(letter)
            }
        }
        return stringBuilder.toString()
    }
}
