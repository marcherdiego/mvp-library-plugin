package com.nerdscorner.mvp.utils;

public class Constants {
    public static final String BUILD = "build";
    public static final String GENERATED = "generated";

    public static class EventBus {
        public static final String FULLY_QUALIFIED_NAME = "org.greenrobot.eventbus.EventBus";
        public static final String CLASS_NAME = "EventBus";
        public static final String INSTANCE_CREATION = "EventBus.getDefault()";
    }

    public static class Properties {
        public static final String PROPERTY_PACKAGE_NAME = "package_name";
        public static final String PROPERTY_COMPONENT_TYPE = "component_type";
        public static final String PROPERTY_LANGUAGE = "language";
        public static final String PROPERTY_COMMUNICATIONS = "communications";

        public static final String COMPONENT_TYPE_ACTIVITY = "Activity";
        public static final String COMPONENT_TYPE_FRAGMENT = "Fragment";

        public static final String LANGUAGE_JAVA = "Java";
        public static final String LANGUAGE_KOTLIN = "Kotlin";

        public static final String COMMUNICATIONS_EVENTS = "Events";
        public static final String COMMUNICATIONS_INTERFACES = "Interfaces";
    }
}
