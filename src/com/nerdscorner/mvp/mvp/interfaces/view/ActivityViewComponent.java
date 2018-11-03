package com.nerdscorner.mvp.mvp.interfaces.view;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class ActivityViewComponent extends MvpComponent {
    private static final String BASE_INTERFACE_VIEW_TEMPLATE = "/templates/interfaces/" + LANGUAGE_PLACEHOLDER + "/interface_view_template";
    private static final String VIEW_TEMPLATE = "/templates/interfaces/" + LANGUAGE_PLACEHOLDER + "/activity_view_template";
    private static final String VIEW_JAVA = "ViewInterface." + LANGUAGE_EXTENSION_PLACEHOLDER;
    private static final String VIEW_IMPL_JAVA = "View." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "view";

    public ActivityViewComponent(String basePath, String basePackage, String screenName) {
        super(BASE_INTERFACE_VIEW_TEMPLATE, screenName + VIEW_JAVA, basePath, basePackage, screenName, VIEW_TEMPLATE, screenName + VIEW_IMPL_JAVA);
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
