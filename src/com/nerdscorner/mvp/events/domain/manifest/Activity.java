package com.nerdscorner.mvp.events.domain.manifest;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Activity {
    private String fullName;
    private String name;
    private String displayName;

    public String getName() {
        return removeActivitySuffix(name);
    }

    public String getUntrimmedName() {
        return name;
    }

    @XmlAttribute(namespace = "http://schemas.android.com/apk/res/android")
    public void setName(String name) {
        this.displayName = name;
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex == -1) {
            return;
        }
        this.name = name.substring(dotIndex + 1);
        this.fullName = name;
    }

    private String removeActivitySuffix(String activity) {
        int activityIndex = activity.lastIndexOf("Activity");
        if (activityIndex == -1) {
            activityIndex = activity.lastIndexOf("activity");
            if (activityIndex == -1) {
                return activity;
            }
        }
        return activity.substring(0, activityIndex);
    }

    @Override
    public String toString() {
        return displayName.substring(displayName.lastIndexOf(".") + 1);
    }

    public String getFullName(String basePackage) {
        return fullName == null ? null : fullName.startsWith(".") ? basePackage + fullName : fullName;
    }
}
