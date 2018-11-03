package com.nerdscorner.mvp.mvp.interfaces.presenter;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class FragmentPresenterComponent extends MvpComponent {
    private static final String PRESENTER_TEMPLATE = "/templates/interfaces/" + LANGUAGE_PLACEHOLDER + "/fragment_presenter_template";
    private static final String PRESENTER_JAVA = "Presenter." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "presenter";

    public FragmentPresenterComponent(String basePath, String basePackage, String screenName) {
        super(null, null, basePath, basePackage, screenName, PRESENTER_TEMPLATE, screenName + PRESENTER_JAVA);
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
