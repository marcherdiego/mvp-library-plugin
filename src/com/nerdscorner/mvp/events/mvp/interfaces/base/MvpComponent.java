package com.nerdscorner.mvp.events.mvp.interfaces.base;

import com.nerdscorner.mvp.events.domain.manifest.Activity;
import com.nerdscorner.mvp.events.mvp.BaseComponent;
import com.nerdscorner.mvp.events.utils.interfaces.FileCreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class MvpComponent extends BaseComponent {
    protected static final String IMPLEMENTATIONS = "implementations";
    protected static final String INTERFACES = "interfaces";

    protected final String baseComponentTemplate;
    protected final String baseComponentDotJava;
    protected final String basePath;
    protected final String basePackage;
    protected final String screenName;
    protected final String componentTemplate;
    protected final String componentDotJava;
    protected String baseComponentFullName;
    protected String baseComponentName;

    public MvpComponent(String baseComponentTemplate, String baseComponentDotJava, String basePath, String basePackage, String screenName,
                        String componentTemplate, String componentDotJava) {
        this.baseComponentTemplate = baseComponentTemplate;
        this.baseComponentDotJava = baseComponentDotJava;
        this.basePath = basePath;
        this.basePackage = basePackage;
        this.screenName = screenName;
        this.componentTemplate = componentTemplate;
        this.componentDotJava = componentDotJava;
    }

    public boolean build() {
        try {
            if (baseComponentDotJava != null && baseComponentTemplate != null) {
                InputStream baseComponentTemplate = getClass().getResourceAsStream(this.baseComponentTemplate);
                File baseComponent = new File(basePath + relativeInterfacePath(), baseComponentDotJava);
                FileCreator.createFile(baseComponentTemplate, baseComponent, basePackage, screenName, baseComponentFullName, baseComponentName);
            }

            //Create screenNameComponentImpl.java
            InputStream componentTemplate = getClass().getResourceAsStream(this.componentTemplate);
            File component = new File(basePath + relativeImplPath(), componentDotJava);
            FileCreator.createFile(componentTemplate, component, basePackage, screenName, baseComponentFullName, baseComponentName);
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
        if (baseComponentDotJava != null) {
            new File(basePath, baseComponentDotJava).delete();
        }
        new File(basePath, screenName + componentDotJava).delete();
    }

    public void setBaseClass(Activity baseClass) {
        this.baseComponentFullName = baseClass.getFullName(basePackage);
        this.baseComponentName = baseClass.getUntrimmedName();
    }
}
