package com.nerdscorner.mvp.mvp.busevents.layout;

import java.io.File;
import java.io.InputStream;

import com.nerdscorner.mvp.mvp.BaseComponent;
import com.nerdscorner.mvp.utils.FileCreator;
import com.nerdscorner.mvp.utils.StringUtils;

public class LayoutComponent extends BaseComponent {
    private static final String LAYOUT_TEMPLATE = "/templates/layouts/layout";
    private static final String DOT = ".";
    private static final String BLANK = "";
    private static final String SOURCE_PATH = "java/";

    private static final String ACTIVITY_SUFFIX = "_activity.xml";
    private static final String FRAGMENT_SUFFIX = "_fragment.xml";

    private final String basePath;
    private final String basePackage;
    private final String screenName;
    private final String suffix;

    public LayoutComponent(String basePath, String basePackage, String screenName, boolean isActivity) {
        String packagePath = basePackage.replace(DOT, File.separator);
        this.basePath = basePath
                .replace(packagePath, BLANK)
                .replace(SOURCE_PATH, BLANK)
                + "/res/layout";
        this.basePackage = basePackage;
        this.screenName = screenName;
        this.suffix = isActivity ? ACTIVITY_SUFFIX : FRAGMENT_SUFFIX;
    }

    public boolean build() {
        try {
            InputStream template = getClass().getResourceAsStream(LAYOUT_TEMPLATE);
            File component = new File(basePath, StringUtils.replaceCamelCaseWithSnakeCase(screenName) + suffix);
            FileCreator.createFile(template, component, basePackage, screenName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void rollback() {
        new File(basePath , StringUtils.replaceCamelCaseWithSnakeCase(screenName) + suffix).delete();
    }
}
