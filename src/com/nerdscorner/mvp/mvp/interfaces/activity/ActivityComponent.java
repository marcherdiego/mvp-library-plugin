package com.nerdscorner.mvp.mvp.interfaces.activity;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class ActivityComponent extends MvpComponent {

    private static final String ACTIVITY_TEMPLATE = "/templates/interfaces/activity_template";
    private static final String ACTIVITY_JAVA = "Activity.java";

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "activities";

    public ActivityComponent(String basePath, String basePackage, String screenName) {
        super(null, null, basePath, basePackage, screenName,
                ACTIVITY_TEMPLATE, screenName + ACTIVITY_JAVA);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }

    @Override
    protected String relativeImplPath() {
        return RELATIVE_PATH;
    }

    @Override
    protected String relativeInterfacePath() {
        return RELATIVE_PATH;
    }
}
