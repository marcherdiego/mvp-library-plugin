package com.nerdscorner.mvp.mvp.interfaces.view;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class ViewComponent extends MvpComponent {
    private static final String BASE_INTERFACE_VIEW_TEMPLATE = "/templates/interfaces/interface_view_template";
    private static final String VIEW_TEMPLATE = "/templates/interfaces/view_template";
    private static final String VIEW_JAVA = "ViewInterface.java";
    private static final String VIEW_IMPL_JAVA = "View.java";

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "mvp" + File.separator + "view";

    public ViewComponent(String basePath, String basePackage, String screenName) {
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
