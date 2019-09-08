package com.nerdscorner.mvp.mvp.busevents.presenter;

import java.io.File;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

public class FragmentPresenterComponent extends MvpComponent {
    private static final String PRESENTER_TEMPLATE = LANGUAGE_PLACEHOLDER + "/fragment_presenter_template";
    private static final String PRESENTER_JAVA = "Presenter." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "presenter";

    public FragmentPresenterComponent(String basePath, String basePackage, String screenName, boolean shouldCreateWiring) {
        super(basePath, basePackage, screenName, PRESENTER_TEMPLATE, screenName + PRESENTER_JAVA, shouldCreateWiring);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
