package com.nerdscorner.mvp.mvp.busevents.base

import com.nerdscorner.mvp.domain.ExecutionResult
import com.nerdscorner.mvp.mvp.BaseComponent
import com.nerdscorner.mvp.utils.FileCreator
import java.io.File

abstract class MvpComponent(
        protected val basePath: String,
        protected val basePackage: String,
        protected val screenName: String,
        componentTemplate: String,
        private var componentWithExtension: String,
        shouldCreateWiring: Boolean
) : BaseComponent() {

    private var componentTemplate: String = getTemplatePath(shouldCreateWiring, componentTemplate)

    private fun getTemplatePath(shouldCreateWiring: Boolean, componentTemplate: String): String {
        return "/templates/" + (if (shouldCreateWiring) "wiring/" else "clean/") + componentTemplate
    }

    fun build(isJava: Boolean): ExecutionResult {
        try {
            //Create {$screenName}{$Component}.{$languageExtension}
            componentTemplate = componentTemplate.replace(
                    LANGUAGE_PLACEHOLDER,
                    if (isJava) {
                        JAVA_FOLDER
                    } else {
                        KOTLIN_FOLDER
                    }
            )
            componentWithExtension = componentWithExtension.replace(
                    LANGUAGE_EXTENSION_PLACEHOLDER,
                    if (isJava) {
                        JAVA_EXTENSION
                    } else {
                        KOTLIN_EXTENSION
                    }
            )
            val template = javaClass.getResourceAsStream(componentTemplate) ?: return ExecutionResult(false)
            val component = File(absolutePath(), componentWithExtension)
            FileCreator.createFile(template, component, basePackage, screenName)
            return ExecutionResult(true)
        } catch (e: Exception) {
            e.printStackTrace()
            return ExecutionResult(false, e.message)
        }
    }

    protected abstract fun relativePath(): String

    private fun absolutePath() = basePath + relativePath()

    override fun rollback() {
        try {
            File(absolutePath(), componentWithExtension).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
