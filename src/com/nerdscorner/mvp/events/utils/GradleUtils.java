package com.nerdscorner.mvp.events.utils;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.Nullable;

public final class GradleUtils {
    public static final String LIBRARY_DEPENDENCY_PACKAGE = "com.nerdscorner.mvp:";

    public static final String MVP_LIB_INTERFACES_DEPENDENCY = "'com.nerdscorner.mvp:interfaces:1.1.0'";
    public static final String MVP_LIB_EVENTS_DEPENDENCY = "'com.nerdscorner.mvp:events:1.3.0'";

    private static final String COMPILE = "\tcompile ";
    private static final String DEPENDENCIES_BLOCK_BEGIN = "dependencies {";
    private static final String DEPENDENCIES_BLOCK_BEGIN_ALT = "dependencies{";

    public static boolean addDependency(VirtualFile rootFolder, String dependency) {
        try {
            VirtualFile appGradleFile = getAppGradleFile(rootFolder);
            String gradleFilegradleFileContent = getGradleFileContent(rootFolder);
            int dependenciesBlockBegin = gradleFilegradleFileContent.indexOf(DEPENDENCIES_BLOCK_BEGIN);
            if (dependenciesBlockBegin == -1) {
                dependenciesBlockBegin = gradleFilegradleFileContent.indexOf(DEPENDENCIES_BLOCK_BEGIN_ALT);
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
                    gradleFileBuilder.append(COMPILE).append(dependency).append(System.lineSeparator());
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
}