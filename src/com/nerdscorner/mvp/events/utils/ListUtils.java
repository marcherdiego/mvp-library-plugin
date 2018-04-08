package com.nerdscorner.mvp.events.utils;

import java.util.List;

public class ListUtils {
    public static boolean isEmpty(List ghReleases) {
        return ghReleases == null || ghReleases.isEmpty();
    }
}
