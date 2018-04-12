package com.nerdscorner.mvp.utils.busevents;

import com.nerdscorner.mvp.utils.Constants.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileCreator {

    private static final String PACKAGE_NAME_KEY = "$PACKAGE_NAME$";
    private static final String SCREEN_NAME_KEY = "$SCREEN_NAME$";

    //Event bus
    private static final String EVENT_BUS_LIBRARY = "$EVENT_BUS_LIBRARY$";
    private static final String EVENT_BUS_CLASS = "$EVENT_BUS_CLASS$";
    private static final String EVENT_BUS_CLASS_INSTANCE = "$EVENT_BUS_CLASS_INSTANCE$";

    public static void createFile(InputStream inputStream, File file, String basePackage, String screenName) throws IOException {
        if (!file.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder baseComponentContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                baseComponentContent.append(line).append(System.lineSeparator());
            }

            //Parameters replacer
            String parsedContent = baseComponentContent
                    .toString()
                    .replace(PACKAGE_NAME_KEY, basePackage)
                    .replace(SCREEN_NAME_KEY, screenName)
                    .replace(EVENT_BUS_LIBRARY, getEventBusLibrary())
                    .replace(EVENT_BUS_CLASS, getEventBusClass())
                    .replace(EVENT_BUS_CLASS_INSTANCE, getEventBusInstanceConstructor());

            file.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(parsedContent);
            fileWriter.close();
        }
    }

    public static void copyFile(InputStream inputStream, File file) throws IOException {
        if (!file.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent.toString());
            fileWriter.close();
        }
    }

    private static String getEventBusLibrary() {
        return EventBus.FULLY_QUALIFIED_NAME;
    }

    private static String getEventBusClass() {
        return EventBus.CLASS_NAME;
    }

    private static String getEventBusInstanceConstructor() {
        return EventBus.INSTANCE_CREATION;
    }
}
