package com.nerdscorner.mvp.utils;

import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileVisitor;
import com.nerdscorner.mvp.domain.manifest.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectUtils {
    private static final String JAVA_FILE_EXTENSION = "java";
    private static final String KOTLIN_FILE_EXTENSION = "kt";

    private static final String JAVA_FRAGMENT_REGEX = ".*extends.*Fragment.*";
    private static final String KOTLIN_FRAGMENT_REGEX = ".*:.*Fragment.*";

    private static final Pattern JAVA_FRAGMENT_PATTERN = Pattern.compile(JAVA_FRAGMENT_REGEX, Pattern.CASE_INSENSITIVE);
    private static final Pattern KOTLIN_FRAGMENT_PATTERN = Pattern.compile(KOTLIN_FRAGMENT_REGEX, Pattern.CASE_INSENSITIVE);

    public static void getFragments(VirtualFile root, final List<Fragment> resultList) {
        VfsUtilCore.visitChildrenRecursively(root, new VirtualFileVisitor<VirtualFile>() {
            @Override
            public boolean visitFile(@NotNull VirtualFile file) {
                if (!file.isDirectory()) {
                    String fileExtension = file.getExtension();
                    if (fileExtension != null) {
                        if (fileExtension.equals(JAVA_FILE_EXTENSION)) {
                            addIfFragment(file, resultList, Type.JAVA);
                            return false;
                        } else if (fileExtension.equals(KOTLIN_FILE_EXTENSION)) {
                            addIfFragment(file, resultList, Type.KOTLIN);
                            return false;
                        }
                    }
                }
                return super.visitFile(file);
            }
        });
    }

    private static void addIfFragment(VirtualFile file, List<Fragment> resultList, int type) {
        try {
            String content = FileReader.getFileContents(new FileInputStream(file.getPath()));
            Matcher matcher = (type == Type.JAVA ? JAVA_FRAGMENT_PATTERN : KOTLIN_FRAGMENT_PATTERN).matcher(content);
            if (matcher.find()) {
                String fileName = file.getName();
                resultList.add(new Fragment().setName(fileName.substring(0, fileName.lastIndexOf("."))));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Type {
        static final int JAVA = 1;
        static final int KOTLIN = 2;
    }
}
