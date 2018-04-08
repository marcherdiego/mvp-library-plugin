package com.nerdscorner.mvp.events.mvp.busevents.view;

import com.nerdscorner.mvp.events.mvp.busevents.base.MvpComponent;

import java.io.File;

public class ViewComponent extends MvpComponent {
    private static final String VIEW_TEMPLATE = "/templates/busevents/view_template";
    private static final String VIEW_JAVA = "View.java";

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "view";

    public ViewComponent(String basePath, String basePackage, String screenName) {
        super(basePath, basePackage, screenName, VIEW_TEMPLATE, screenName + VIEW_JAVA);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
