package com.nerdscorner.mvp.mvp.interfaces.base;

import com.nerdscorner.mvp.mvp.BaseComponent;
import com.nerdscorner.mvp.utils.interfaces.FileCreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class MvpComponent extends BaseComponent {
    protected static final String IMPLEMENTATIONS = "implementations";
    protected static final String INTERFACES = "interfaces";

    protected final String basePath;
    protected final String basePackage;
    protected final String screenName;

    private String baseComponentTemplate;
    private String baseComponentWithExtension;

    private String componentTemplate;
    private String componentWithExtension;

    public MvpComponent(String baseComponentTemplate, String baseComponentWithExtension, String basePath, String basePackage,
                        String screenName, String componentTemplate, String componentWithExtension) {
        this.baseComponentTemplate = baseComponentTemplate;
        this.baseComponentWithExtension = baseComponentWithExtension;
        this.basePath = basePath;
        this.basePackage = basePackage;
        this.screenName = screenName;
        this.componentTemplate = componentTemplate;
        this.componentWithExtension = componentWithExtension;
    }

    public boolean build(boolean isJava) {
        try {
            if (baseComponentWithExtension != null && baseComponentTemplate != null) {
                //Create Base{$screenName}{$Component}.{$languageExtension}
                baseComponentTemplate = baseComponentTemplate.replace(LANGUAGE_PLACEHOLDER, isJava ? JAVA_FOLDER : KOTLIN_FOLDER);
                baseComponentWithExtension = baseComponentWithExtension.replace(LANGUAGE_EXTENSION_PLACEHOLDER, isJava ? JAVA_EXTENSION : KOTLIN_EXTENSION);
                InputStream baseComponentTemplate = getClass().getResourceAsStream(this.baseComponentTemplate);
                File baseComponent = new File(basePath + relativeInterfacePath(), baseComponentWithExtension);
                FileCreator.createFile(baseComponentTemplate, baseComponent, basePackage, screenName);
            }

            //Create {$screenName}{$Component}.{$languageExtension}
            componentTemplate = componentTemplate.replace(LANGUAGE_PLACEHOLDER, isJava ? JAVA_FOLDER : KOTLIN_FOLDER);
            componentWithExtension = componentWithExtension.replace(LANGUAGE_EXTENSION_PLACEHOLDER, isJava ? JAVA_EXTENSION : KOTLIN_EXTENSION);
            InputStream componentTemplate = getClass().getResourceAsStream(this.componentTemplate);
            File component = new File(basePath + relativeImplPath(), componentWithExtension);
            FileCreator.createFile(componentTemplate, component, basePackage, screenName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected abstract String relativePath();

    protected abstract String relativeImplPath();

    protected abstract String relativeInterfacePath();

    @Override
    public void rollback() {
        if (baseComponentWithExtension != null) {
            new File(basePath, baseComponentWithExtension).delete();
        }
        new File(basePath, screenName + componentWithExtension).delete();
    }
}
