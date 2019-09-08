package com.nerdscorner.mvp.mvp.busevents.view;

import java.io.File;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

public class ActivityViewComponent extends MvpComponent {
    private static final String VIEW_TEMPLATE = LANGUAGE_PLACEHOLDER + "/activity_view_template";
    private static final String VIEW_JAVA = "View." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "view";

    public ActivityViewComponent(String basePath, String basePackage, String screenName, boolean shouldCreateWiring) {
        super(basePath, basePackage, screenName, VIEW_TEMPLATE, screenName + VIEW_JAVA, shouldCreateWiring);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
