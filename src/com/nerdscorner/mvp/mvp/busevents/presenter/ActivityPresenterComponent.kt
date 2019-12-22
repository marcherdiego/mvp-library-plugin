package com.nerdscorner.mvp.mvp.busevents.presenter

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent
import java.io.File

class ActivityPresenterComponent(basePath: String, basePackage: String, screenName: String, shouldCreateWiring: Boolean)
    : MvpComponent(basePath, basePackage, screenName, PRESENTER_TEMPLATE, screenName + PRESENTER_JAVA, shouldCreateWiring) {

    override fun relativePath() = RELATIVE_PATH

    companion object {
        private const val PRESENTER_TEMPLATE = "$LANGUAGE_PLACEHOLDER/activity_presenter_template"
        private const val PRESENTER_JAVA = "Presenter.$LANGUAGE_EXTENSION_PLACEHOLDER"

        private val RELATIVE_PATH = "${File.separator}ui${File.separator}mvp${File.separator}presenter"
    }
}
