package com.nerdscorner.mvp.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Nullable;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;

public final class GradleUtils {
    private static final String LATEST_EVENTS_LIB_VERSION = "4.0.0";

    public static final String MVP_LIB_EVENTS_DEPENDENCY_PKG = "'com.nerdscorner.mvp:events:";
    public static final String MVP_LIB_EVENTS_DEPENDENCY = MVP_LIB_EVENTS_DEPENDENCY_PKG + LATEST_EVENTS_LIB_VERSION + "'";

    private static final String IMPLEMENTATION = "\timplementation ";
    private static final String DEPENDENCIES_BLOCK_BEGIN = "dependencies {";
    private static final String DEPENDENCIES_BLOCK_BEGIN_ALT = "dependencies{";

    public static boolean addDependency(VirtualFile rootFolder, String dependency) {
        try {
            VirtualFile appGradleFile = getAppGradleFile(rootFolder);
            String gradleFileContent = getGradleFileContent(rootFolder);
            int dependenciesBlockBegin = gradleFileContent.indexOf(DEPENDENCIES_BLOCK_BEGIN);
            if (dependenciesBlockBegin == -1) {
                dependenciesBlockBegin = gradleFileContent.indexOf(DEPENDENCIES_BLOCK_BEGIN_ALT);
                if (dependenciesBlockBegin == -1) {
                    throw new IllegalStateException("Malformed build.gradle file, dependencies block not recognized. Expected dependencies { ... }");
                }
            }

            final int SEEKING = 0;
            final int DONE = 2;
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(appGradleFile.contentsToByteArray())));
            String line;
            StringBuilder gradleFileBuilder = new StringBuilder();
            int state = SEEKING;
            while ((line = reader.readLine()) != null) {
                gradleFileBuilder
                        .append(line)
                        .append(System.lineSeparator());
                if (state == DONE) {
                    continue;
                }
                if (line.contains(DEPENDENCIES_BLOCK_BEGIN) || line.contains(DEPENDENCIES_BLOCK_BEGIN_ALT)) {
                    gradleFileBuilder.append(IMPLEMENTATION).append(dependency).append(System.lineSeparator());
                    state = DONE;
                }
            }
            FileWriter fileWriter = new FileWriter(new File(getAppGradleFile(rootFolder).getPath()));
            fileWriter.write(gradleFileBuilder.toString());
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    public static String getGradleFileContent(VirtualFile rootFolder) {
        VirtualFile appGradleFile = getAppGradleFile(rootFolder);
        try {
            return FileReader.getFileContents(new FileInputStream(appGradleFile.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static VirtualFile getAppGradleFile(VirtualFile rootFolder) {
        return rootFolder
                .getParent()
                .getParent()
                .getParent()
                .findChild("build.gradle");
    }

    public static boolean hasDependency(VirtualFile rootFolder, String dependency) {
        return getGradleFileContent(rootFolder).contains(dependency);
    }

    public static void restoreGradleFile(String savedGradleFile, VirtualFile rootFolder) {
        if (savedGradleFile == null) {
            return;
        }
        try {
            File manifestFile = new File(getAppGradleFile(rootFolder).getPath());
            FileWriter fileWriter = new FileWriter(manifestFile);
            fileWriter.write(savedGradleFile);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void performSync(AnActionEvent actionEvent) {
        ActionManager am = ActionManager.getInstance();
        AnAction sync = am.getAction("Android.SyncProject");
        sync.actionPerformed(actionEvent);
    }
}
