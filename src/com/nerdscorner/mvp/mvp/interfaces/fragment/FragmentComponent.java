package com.nerdscorner.mvp.mvp.interfaces.fragment;

import com.nerdscorner.mvp.mvp.interfaces.base.MvpComponent;

import java.io.File;

public class FragmentComponent extends MvpComponent {
    private static final String FRAGMENT_TEMPLATE = "/templates/interfaces/" + LANGUAGE_PLACEHOLDER + "/fragment_template";
    private static final String FRAGMENT_JAVA = "Fragment." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "fragments";

    public FragmentComponent(String basePath, String basePackage, String screenName) {
        super(null, null, basePath, basePackage, screenName, FRAGMENT_TEMPLATE, screenName + FRAGMENT_JAVA);
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
