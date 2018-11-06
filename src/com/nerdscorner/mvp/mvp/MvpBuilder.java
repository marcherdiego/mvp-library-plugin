package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;

public abstract class MvpBuilder {
    protected final boolean shouldIncludeLibraryDependency;
    protected final boolean isJava;
    protected String savedGradleFile;

    public MvpBuilder(boolean shouldIncludeLibraryDependency, boolean isJava) {
        this.shouldIncludeLibraryDependency = shouldIncludeLibraryDependency;
        this.isJava = isJava;
    }

    public abstract boolean build(VirtualFile rootFolder, String fullPath, String packageName, String screenName,
                                  boolean interfaces, boolean isExistingScreen);
}
