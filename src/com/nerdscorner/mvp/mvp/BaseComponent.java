package com.nerdscorner.mvp.mvp;

public abstract class BaseComponent {

    public static final String LANGUAGE_PLACEHOLDER = "LANGUAGE";
    public static final String LANGUAGE_EXTENSION_PLACEHOLDER = "LANGUAGE_EXTENSION";

    public static final String JAVA_FOLDER = "java";
    public static final String KOTLIN_FOLDER = "kotlin";
    public static final String JAVA_EXTENSION = "java";
    public static final String KOTLIN_EXTENSION = "kt";

    public abstract void rollback();
}
