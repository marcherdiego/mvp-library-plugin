package com.nerdscorner.mvp.mvp.busevents.model;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

import java.io.File;

public class ModelComponent extends MvpComponent {
    private static final String MODEL_TEMPLATE = "/templates/busevents/" + LANGUAGE_PLACEHOLDER + "/model_template";
    private static final String MODEL_JAVA = "Model." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "model";

    public ModelComponent(String basePath, String basePackage, String screenName) {
        super(basePath, basePackage, screenName, MODEL_TEMPLATE, screenName + MODEL_JAVA);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
