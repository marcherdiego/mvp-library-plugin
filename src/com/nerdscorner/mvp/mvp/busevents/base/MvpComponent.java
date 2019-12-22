package com.nerdscorner.mvp.mvp.busevents.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.nerdscorner.mvp.mvp.BaseComponent;
import com.nerdscorner.mvp.utils.FileCreator;

public abstract class MvpComponent extends BaseComponent {

    protected final String basePath;
    protected final String basePackage;
    protected final String screenName;

    private String componentTemplate;
    private String componentWithExtension;

    public MvpComponent(String basePath, String basePackage, String screenName, String componentTemplate, String componentWithExtension,
                        boolean shouldCreateWiring) {
        this.basePath = basePath;
        this.basePackage = basePackage;
        this.screenName = screenName;
        this.componentTemplate = getTemplatePath(shouldCreateWiring, componentTemplate);
        this.componentWithExtension = componentWithExtension;
    }

    private String getTemplatePath(boolean shouldCreateWiring, String componentTemplate) {
        return "/templates/" + (shouldCreateWiring ? "wiring/" : "clean/") + componentTemplate;
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
