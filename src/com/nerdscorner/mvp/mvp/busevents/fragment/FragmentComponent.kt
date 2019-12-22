package com.nerdscorner.mvp.mvp.busevents.fragment

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent
import java.io.File

class FragmentComponent(basePath: String, basePackage: String, screenName: String, shouldCreateWiring: Boolean)
    : MvpComponent(basePath, basePackage, screenName, FRAGMENT_TEMPLATE, screenName + FRAGMENT_JAVA, shouldCreateWiring) {

    override fun relativePath() = RELATIVE_PATH

    companion object {
        private const val FRAGMENT_TEMPLATE = "$LANGUAGE_PLACEHOLDER/fragment_template"
        private const val FRAGMENT_JAVA = "Fragment.$LANGUAGE_EXTENSION_PLACEHOLDER"

        private val RELATIVE_PATH = "${File.separator}ui${File.separator}fragments"
    }
}
