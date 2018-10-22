package com.nerdscorner.mvp.mvp;

import com.intellij.openapi.vfs.VirtualFile;

public abstract class MvpBuilder {

    protected final VirtualFile rootFolder;
    protected final String fullPath;
    protected final String packageName;
    protected final String screenName;
    protected final boolean interfaces;
    protected final boolean shouldIncludeLibraryDependency;
    protected String savedGradleFile;

    public MvpBuilder(VirtualFile rootFolder, String fullPath, String packageName, String screenName, boolean interfaces,
                      boolean shouldIncludeLibraryDependency) {
        this.rootFolder = rootFolder;
        this.fullPath = fullPath;
        this.packageName = packageName;
        this.screenName = screenName;
        this.interfaces = interfaces;
        this.shouldIncludeLibraryDependency = shouldIncludeLibraryDependency;
    }

    public abstract boolean build();
}
