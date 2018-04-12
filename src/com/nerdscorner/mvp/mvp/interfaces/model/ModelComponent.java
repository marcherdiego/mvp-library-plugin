package com.nerdscorner.mvp.mvp.interfaces.model;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class ModelComponent extends MvpComponent {
    private static final String BASE_MODEL_TEMPLATE = "/templates/interfaces/interface_model_template";
    private static final String MODEL_TEMPLATE = "/templates/interfaces/model_template";
    private static final String BASE_MODEL_JAVA = "ModelInterface.java";
    private static final String MODEL_JAVA = "Model.java";

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "model";

    public ModelComponent(String basePath, String basePackage, String screenName) {
        super(BASE_MODEL_TEMPLATE, screenName + BASE_MODEL_JAVA, basePath, basePackage, screenName, MODEL_TEMPLATE, screenName + MODEL_JAVA);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }

    @Override
    protected String relativeImplPath() {
        return RELATIVE_PATH + File.separator + IMPLEMENTATIONS;
    }

    @Override
    protected String relativeInterfacePath() {
        return RELATIVE_PATH + File.separator + INTERFACES;
    }
}
