package com.nerdscorner.mvp.mvp.busevents.base

import com.nerdscorner.mvp.mvp.BaseComponent
import com.nerdscorner.mvp.utils.FileCreator
import java.io.File
import java.io.IOException

abstract class MvpComponent(
        protected val basePath: String, protected val basePackage: String, protected val screenName: String, componentTemplate: String,
        private var componentWithExtension: String?, shouldCreateWiring: Boolean) : BaseComponent() {

    private var componentTemplate: String = getTemplatePath(shouldCreateWiring, componentTemplate)

    private fun getTemplatePath(shouldCreateWiring: Boolean, componentTemplate: String): String {
        return "/templates/" + (if (shouldCreateWiring) "wiring/" else "clean/") + componentTemplate
    }

    fun build(isJava: Boolean): Boolean {
        try {
            //Create {$screenName}{$Component}.{$languageExtension}
            componentTemplate = componentTemplate.replace(LANGUAGE_PLACEHOLDER, if (isJava) JAVA_FOLDER else KOTLIN_FOLDER)
            componentWithExtension = componentWithExtension?.replace(LANGUAGE_EXTENSION_PLACEHOLDER, if (isJava) JAVA_EXTENSION else KOTLIN_EXTENSION)
            val template = javaClass.getResourceAsStream(componentTemplate)
            val component = File(basePath + relativePath(), componentWithExtension)
            FileCreator.createFile(template, component, basePackage, screenName)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    protected abstract fun relativePath(): String

    override fun rollback() {
        File(basePath, screenName + componentWithExtension).delete()
    }
}
