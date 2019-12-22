package com.nerdscorner.mvp.mvp.busevents.model

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent
import java.io.File

class ModelComponent(basePath: String, basePackage: String, screenName: String, shouldCreateWiring: Boolean)
    : MvpComponent(basePath, basePackage, screenName, MODEL_TEMPLATE, screenName + MODEL_JAVA, shouldCreateWiring) {

    override fun relativePath() = RELATIVE_PATH

    companion object {
        private const val MODEL_TEMPLATE = "$LANGUAGE_PLACEHOLDER/model_template"
        private const val MODEL_JAVA = "Model.$LANGUAGE_EXTENSION_PLACEHOLDER"

        private val RELATIVE_PATH = "${File.separator}ui${File.separator}mvp${File.separator}model"
    }
}
