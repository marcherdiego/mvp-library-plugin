package com.nerdscorner.mvp.mvp

abstract class BaseComponent {

    abstract fun rollback()

    companion object {
        const val LANGUAGE_PLACEHOLDER = "LANGUAGE"
        const val LANGUAGE_EXTENSION_PLACEHOLDER = "LANGUAGE_EXTENSION"

        const val JAVA_FOLDER = "java"
        const val KOTLIN_FOLDER = "kotlin"
        const val JAVA_EXTENSION = "java"
        const val KOTLIN_EXTENSION = "kt"
    }
}
