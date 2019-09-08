package com.nerdscorner.mvp.mvp.busevents.activity;

import java.io.File;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

public class ActivityComponent extends MvpComponent {
    public static final String PACKAGE_SUFFIX = ".ui.activities";
    public static final String ACTIVITY_SUFFIX = "Activity";

    private static final String ACTIVITY_TEMPLATE = LANGUAGE_PLACEHOLDER + "/activity_template";
    private static final String ACTIVITY_JAVA = "Activity." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "activities";

    public ActivityComponent(String basePath, String basePackage, String screenName, boolean shouldCreateWiring) {
        super(basePath, basePackage, screenName, ACTIVITY_TEMPLATE, screenName + ACTIVITY_JAVA, shouldCreateWiring);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
