package com.nerdscorner.mvp.mvp.interfaces.presenter;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class PresenterComponent extends MvpComponent {
    private static final String BASE_PRESENTER_TEMPLATE = "/templates/interfaces/java/interface_presenter_template";
    private static final String PRESENTER_TEMPLATE = "/templates/interfaces/java/presenter_template";
    private static final String BASE_PRESENTER_JAVA = "PresenterInterface.java";
    private static final String PRESENTER_JAVA = "Presenter.java";

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "presenter";

    public PresenterComponent(String basePath, String basePackage, String screenName) {
        super(BASE_PRESENTER_TEMPLATE, screenName + BASE_PRESENTER_JAVA, basePath, basePackage, screenName, PRESENTER_TEMPLATE, screenName + PRESENTER_JAVA);
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
