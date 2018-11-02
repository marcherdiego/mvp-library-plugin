package com.nerdscorner.mvp.mvp.busevents.base;

import com.nerdscorner.mvp.mvp.BaseComponent;
import com.nerdscorner.mvp.utils.busevents.FileCreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class MvpComponent extends BaseComponent {
    public static final String JAVA_FOLDER = "java";
    public static final String KOTLIN_FOLDER = "kotlin";
    public static final String JAVA_EXTENSION = "java";
    public static final String KOTLIN_EXTENSION = "kt";

    public static final String LANGUAGE_PLACEHOLDER = "LANGUAGE";
    public static final String LANGUAGE_EXTENSION_PLACEHOLDER = "LANGUAGE_EXTENSION";

    protected final String basePath;
    protected final String basePackage;
    protected final String screenName;

    private String componentTemplate;
    private String componentWithExtension;

    public MvpComponent(String basePath, String basePackage, String screenName, String componentTemplate, String componentWithExtension) {
        this.basePath = basePath;
        this.basePackage = basePackage;
        this.screenName = screenName;
        this.componentTemplate = componentTemplate;
        this.componentWithExtension = componentWithExtension;
    }

    public boolean build(boolean isJava) {
        try {
            //Create {$screenName}{$Component}.{$languageExtension}
            componentTemplate = componentTemplate.replace(LANGUAGE_PLACEHOLDER, isJava ? JAVA_FOLDER : KOTLIN_FOLDER);
            componentWithExtension = componentWithExtension.replace(LANGUAGE_EXTENSION_PLACEHOLDER, isJava ? JAVA_EXTENSION : KOTLIN_EXTENSION);
            InputStream template = getClass().getResourceAsStream(componentTemplate);
            File component = new File(basePath + relativePath(), componentWithExtension);
            FileCreator.createFile(template, component, basePackage, screenName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected abstract String relativePath();

    @Override
    public void rollback() {
        new File(basePath, screenName + componentWithExtension).delete();
    }
}
