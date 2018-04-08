package com.nerdscorner.mvp.events.utils;

import com.intellij.openapi.vfs.VirtualFile;
import com.nerdscorner.mvp.events.domain.manifest.Activity;
import com.nerdscorner.mvp.events.domain.manifest.Manifest;
import com.nerdscorner.mvp.events.mvp.busevents.activity.ActivityComponent;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ManifestUtils {
    private static final String XML_ACTIVITY_START_TAG = "<application";
    private static final String XML_CLOSE_TAG = ">";

    private static final String PACKAGE_NAME = "$PACKAGE_NAME$";
    private static final String ACTIVITY_NAME = "$ACTIVITY_NAME$";
    private static final String NEW_LINE = "$NEW_LINE$";
    private static final String MANIFEST_ACTIVITY_TEMPLATE = "\t\t<activity android:name=\"$PACKAGE_NAME$.$ACTIVITY_NAME$\">$NEW_LINE$\t\t</activity>";

    public static Activity[] findActivities(VirtualFile sourceFolder) {
        Manifest manifest = getManifest(sourceFolder);
        if (manifest == null) {
            return null;
        }
        return manifest.getApplication().getActivity();
    }

    @Nullable
    public static Manifest getManifest(VirtualFile sourceFolder) {
        try {
            sourceFolder.refresh(false, true);
            JAXBContext jaxbContext = JAXBContext.newInstance(Manifest.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            VirtualFile manifestFile = sourceFolder.getParent().findChild("AndroidManifest.xml");
            return (Manifest) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(manifestFile.contentsToByteArray()));
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getManifestString(VirtualFile sourceFolder) {
        try {
            sourceFolder.refresh(false, true);
            VirtualFile manifestFile = sourceFolder.getParent().findChild("AndroidManifest.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(manifestFile.contentsToByteArray())));
            String line;
            StringBuilder manifestFileBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                manifestFileBuilder
                        .append(line)
                        .append(System.lineSeparator());
            }
            return manifestFileBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addActivityToManifest(String packageName, String activityName, VirtualFile sourceFolder) {
        packageName = packageName + ActivityComponent.PACKAGE_SUFFIX;
        activityName = activityName + ActivityComponent.ACTIVITY_SUFFIX;
        final int SEEKING = 0;
        final int WAITING_FOR_ACTIVITY_CLOSE = 1;
        final int DONE = 2;
        try {
            sourceFolder.refresh(false, true);
            VirtualFile manifestFile = sourceFolder.getParent().findChild("AndroidManifest.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(manifestFile.contentsToByteArray())));
            String line;
            StringBuilder manifestFileBuilder = new StringBuilder();
            int state = SEEKING;
            while ((line = reader.readLine()) != null) {
                manifestFileBuilder
                        .append(line)
                        .append(System.lineSeparator());
                if (state == DONE) {
                    continue;
                }
                if (line.contains(XML_ACTIVITY_START_TAG)) {
                    if (line.contains(XML_CLOSE_TAG)) {
                        state = appendActivityAndDone(packageName, activityName, DONE, manifestFileBuilder);
                    } else {
                        state = WAITING_FOR_ACTIVITY_CLOSE;
                    }
                } else {
                    if (state == WAITING_FOR_ACTIVITY_CLOSE && line.contains(XML_CLOSE_TAG)) {
                        state = appendActivityAndDone(packageName, activityName, DONE, manifestFileBuilder);
                    }
                }
            }
            FileWriter fileWriter = new FileWriter(new File(sourceFolder.getPath() + "/../AndroidManifest.xml"));
            fileWriter.write(manifestFileBuilder.toString());
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int appendActivityAndDone(String packageName, String activityName, int state, StringBuilder manifestFileBuilder) {
        manifestFileBuilder.append(
                MANIFEST_ACTIVITY_TEMPLATE
                        .replace(PACKAGE_NAME, packageName)
                        .replace(ACTIVITY_NAME, activityName)
                        .replace(NEW_LINE, System.lineSeparator())
        ).append(System.lineSeparator());
        return state;
    }

    public static void restoreManifest(String savedManifest, String projectRoot) {
        if (savedManifest == null) {
            return;
        }
        try {
            File manifestFile = new File(projectRoot + "/../AndroidManifest.xml");
            FileWriter fileWriter = new FileWriter(manifestFile);
            fileWriter.write(savedManifest);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
