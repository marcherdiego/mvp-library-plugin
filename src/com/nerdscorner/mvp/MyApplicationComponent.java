package com.nerdscorner.mvp;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.components.ApplicationComponent;
import com.nerdscorner.mvp.utils.LibraryUtils;

public class MyApplicationComponent implements ApplicationComponent {

    public void initComponent() {
        LibraryUtils.INSTANCE.fetchLatestVersions();
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "MyApplicationComponent";
    }
}
