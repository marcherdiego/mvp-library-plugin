package com.nerdscorner.mvp.mvp.busevents.view

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent
import java.io.File

class ActivityViewComponent(basePath: String, basePackage: String, screenName: String, shouldCreateWiring: Boolean)
    : MvpComponent(basePath, basePackage, screenName, VIEW_TEMPLATE, screenName + VIEW_JAVA, shouldCreateWiring) {

    override fun relativePath() = RELATIVE_PATH

    companion object {
        private const val VIEW_TEMPLATE = "$LANGUAGE_PLACEHOLDER/activity_view_template"
        private const val VIEW_JAVA = "View.$LANGUAGE_EXTENSION_PLACEHOLDER"

        private val RELATIVE_PATH = "${File.separator}ui${File.separator}mvp${File.separator}view"
    }
}
