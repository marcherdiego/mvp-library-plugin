package com.nerdscorner.mvp.events.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReader {

    public static String getFileContents(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder rawFileContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            rawFileContent.append(line).append(System.lineSeparator());
        }
        return rawFileContent.toString();
    }
}
