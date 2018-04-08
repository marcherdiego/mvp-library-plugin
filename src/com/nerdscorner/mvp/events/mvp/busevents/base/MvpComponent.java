package com.nerdscorner.mvp.events.mvp.busevents.base;

import com.nerdscorner.mvp.events.mvp.BaseComponent;
import com.nerdscorner.mvp.events.utils.busevents.FileCreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class MvpComponent extends BaseComponent {
    protected final String basePath;
    protected final String basePackage;
    protected final String screenName;
    protected final String componentTemplate;
    protected final String componentDotJava;

    public MvpComponent(String basePath, String basePackage, String screenName, String componentTemplate, String componentDotJava) {
        this.basePath = basePath;
        this.basePackage = basePackage;
        this.screenName = screenName;
        this.componentTemplate = componentTemplate;
        this.componentDotJava = componentDotJava;
    }

    public boolean build() {
        try {
            //Create screenNameModel.java
            InputStream componentTemplate = getClass().getResourceAsStream(this.componentTemplate);
            File component = new File(basePath + relativePath(), componentDotJava);
            FileCreator.createFile(componentTemplate, component, basePackage, screenName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected abstract String relativePath();

    @Override
    public void rollback() {
        new File(basePath, screenName + componentDotJava).delete();
    }
}
