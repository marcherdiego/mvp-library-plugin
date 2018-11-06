package com.nerdscorner.mvp.domain.manifest;

public abstract class ScreenComponent {
    protected String name;

    public abstract String getName();

    public static final String NONE = "None";
}
