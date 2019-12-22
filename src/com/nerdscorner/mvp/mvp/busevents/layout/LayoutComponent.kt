package com.nerdscorner.mvp.mvp.busevents.layout

import com.nerdscorner.mvp.mvp.BaseComponent
import com.nerdscorner.mvp.utils.FileCreator
import com.nerdscorner.mvp.utils.StringUtils
import java.io.File

class LayoutComponent(basePath: String, private val basePackage: String, private val screenName: String, isActivity: Boolean)
    : BaseComponent() {

    private val basePath: String
    private val suffix: String

    init {
        val packagePath = basePackage.replace(DOT, File.separator)
        this.basePath = basePath
                .replace(packagePath, BLANK)
                .replace(SOURCE_PATH, BLANK) + "/res/layout"
        this.suffix = if (isActivity) ACTIVITY_SUFFIX else FRAGMENT_SUFFIX
    }

    fun build(): Boolean {
        try {
            val template = javaClass.getResourceAsStream(LAYOUT_TEMPLATE)
            val component = File(basePath, StringUtils.replaceCamelCaseWithSnakeCase(screenName) + suffix)
            FileCreator.createFile(template, component, basePackage, screenName)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun rollback() {
        File(basePath, StringUtils.replaceCamelCaseWithSnakeCase(screenName) + suffix).delete()
    }

    companion object {
        private const val LAYOUT_TEMPLATE = "/templates/layouts/layout"
        private const val DOT = "."
        private const val BLANK = ""
        private const val SOURCE_PATH = "java/"

        private const val ACTIVITY_SUFFIX = "_activity.xml"
        private const val FRAGMENT_SUFFIX = "_fragment.xml"
    }
}
