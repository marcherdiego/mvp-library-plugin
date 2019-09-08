package com.nerdscorner.mvp.mvp.busevents.fragment;

import java.io.File;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

public class FragmentComponent extends MvpComponent {
    private static final String FRAGMENT_TEMPLATE = LANGUAGE_PLACEHOLDER + "/fragment_template";
    private static final String FRAGMENT_JAVA = "Fragment." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "fragments";

    public FragmentComponent(String basePath, String basePackage, String screenName, boolean shouldCreateWiring) {
        super(basePath, basePackage, screenName, FRAGMENT_TEMPLATE, screenName + FRAGMENT_JAVA, shouldCreateWiring);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
