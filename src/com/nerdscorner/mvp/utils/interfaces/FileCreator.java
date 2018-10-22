package com.nerdscorner.mvp.utils.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileCreator {

    private static final String BASE_COMPONENT_FULL_NAME = "$BASE_COMPONENT_FULL_NAME$";
    private static final String BASE_COMPONENT_NAME = "$BASE_COMPONENT_NAME$";
    private static final String PACKAGE_NAME_KEY = "$PACKAGE_NAME$";
    private static final String SCREEN_NAME_KEY = "$SCREEN_NAME$";

    public static void createFile(InputStream inputStream, File file, String basePackage, String screenName, String baseComponentFullName,
                                  String baseComponentName) throws IOException {
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
                    .replace(BASE_COMPONENT_FULL_NAME, baseComponentFullName == null ? "" : "import " + baseComponentFullName + ";")
                    .replace(BASE_COMPONENT_NAME, baseComponentName == null ? "" : " extends " + baseComponentName);

            file.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(parsedContent);
            fileWriter.close();
        }
    }
}
