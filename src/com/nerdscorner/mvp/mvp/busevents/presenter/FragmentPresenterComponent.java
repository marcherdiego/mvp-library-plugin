package com.nerdscorner.mvp.mvp.busevents.presenter;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

import java.io.File;

public class FragmentPresenterComponent extends MvpComponent {
    private static final String PRESENTER_TEMPLATE = "/templates/busevents/" + LANGUAGE_PLACEHOLDER + "/fragment_presenter_template";
    private static final String PRESENTER_JAVA = "Presenter." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "presenter";

    public FragmentPresenterComponent(String basePath, String basePackage, String screenName) {
        super(basePath, basePackage, screenName, PRESENTER_TEMPLATE, screenName + PRESENTER_JAVA);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
