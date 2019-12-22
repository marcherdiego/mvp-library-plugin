package com.nerdscorner.mvp.mvp.busevents.activity

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent
import java.io.File

class ActivityComponent(basePath: String, basePackage: String, screenName: String, shouldCreateWiring: Boolean)
    : MvpComponent(basePath, basePackage, screenName, ACTIVITY_TEMPLATE, screenName + ACTIVITY_JAVA, shouldCreateWiring) {

    override fun relativePath() = RELATIVE_PATH

    companion object {
        const val PACKAGE_SUFFIX = ".ui.activities"
        const val ACTIVITY_SUFFIX = "Activity"

        private const val ACTIVITY_TEMPLATE = "$LANGUAGE_PLACEHOLDER/activity_template"
        private const val ACTIVITY_JAVA = "Activity.$LANGUAGE_EXTENSION_PLACEHOLDER"

        private val RELATIVE_PATH = "${File.separator}ui${File.separator}activities"
    }
}
