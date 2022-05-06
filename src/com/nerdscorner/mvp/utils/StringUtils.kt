package com.nerdscorner.mvp.utils

import com.intellij.codeInsight.template.impl.TemplateSettings.SPACE_CHAR
import java.util.*

object StringUtils {

    private const val UNDERSCORE = "_"

    @JvmStatic
    fun isEmpty(text: String?) = text == null || text.isEmpty()

    @JvmStatic
    fun asCamelCase(text: String?): String? {
        return replaceSpacesWithCamelCase(text)?.also {
            it.substring(0, 1).uppercase(Locale.getDefault()) + it.substring(1)
        }
    }

    private fun replaceSpacesWithCamelCase(text: String?): String? {
        if (text.isNullOrEmpty()) {
            return text
        }
        val stringBuilder = StringBuilder()
        var shouldCapitalizeLetter = false
        for (letter in text) {
            when {
                letter == SPACE_CHAR -> shouldCapitalizeLetter = true
                shouldCapitalizeLetter -> {
                    shouldCapitalizeLetter = false
                    stringBuilder.append(letter.toString().uppercase(Locale.getDefault()))
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
        for (i in text.indices) {
            val letter = text[i]
            if (Character.isUpperCase(letter)) {
                if (i > 0) {
                    stringBuilder.append(UNDERSCORE)
                }
                stringBuilder.append(letter.toString().lowercase())
            } else {
                stringBuilder.append(letter)
            }
        }
        return stringBuilder.toString()
    }
}
