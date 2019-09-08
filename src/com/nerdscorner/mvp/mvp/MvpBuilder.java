package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;

public abstract class MvpBuilder {
    protected final boolean shouldIncludeLibraryDependency;
    protected final boolean shouldCreateWiring;
    protected final boolean isJava;
    protected String savedGradleFile;

    public MvpBuilder(boolean shouldIncludeLibraryDependency, boolean isJava, boolean shouldCreateWiring) {
        this.shouldIncludeLibraryDependency = shouldIncludeLibraryDependency;
        this.isJava = isJava;
        this.shouldCreateWiring = shouldCreateWiring;
    }

    public abstract boolean build(VirtualFile rootFolder, String fullPath, String packageName, String screenName);
}
