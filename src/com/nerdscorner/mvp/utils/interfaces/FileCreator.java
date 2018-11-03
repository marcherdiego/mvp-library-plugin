package com.nerdscorner.mvp.utils.interfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileCreator {

    private static final String PACKAGE_NAME_KEY = "$PACKAGE_NAME$";
    private static final String SCREEN_NAME_KEY = "$SCREEN_NAME$";

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
                    .replace(SCREEN_NAME_KEY, screenName);

            file.getParentFile().mkdirs();
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(parsedContent);
            fileWriter.close();
        }
    }
}
