package com.nerdscorner.mvp.mvp.busevents.fragment;

import com.nerdscorner.mvp.mvp.busevents.base.MvpComponent;

import java.io.File;

public class FragmentComponent extends MvpComponent {
    private static final String FRAGMENT_TEMPLATE = "/templates/busevents/" + LANGUAGE_PLACEHOLDER + "/fragment_template";
    private static final String FRAGMENT_JAVA = "Fragment." + LANGUAGE_EXTENSION_PLACEHOLDER;

    private static final String RELATIVE_PATH = File.separator + "ui" + File.separator + "fragments";

    public FragmentComponent(String basePath, String basePackage, String screenName) {
        super(basePath, basePackage, screenName, FRAGMENT_TEMPLATE, screenName + FRAGMENT_JAVA);
    }

    @Override
    protected String relativePath() {
        return RELATIVE_PATH;
    }
}
