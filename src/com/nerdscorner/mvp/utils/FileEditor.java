package com.nerdscorner.mvp.utils;

import com.nerdscorner.mvp.utils.busevents.FileCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileEditor {

    public static void replaceNextLineWith(InputStream inputStream, File file, String needle, String replacement) throws IOException {
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
                if (line.contains(needle)) {
                    fileContent.append(replacement).append(System.lineSeparator());

                    //Ignore next line, it was replaced
                    if (reader.readLine() == null) {
                        break;
                    }
                }
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileContent.toString());
            fileWriter.close();
        } else {
            FileCreator.copyFile(inputStream, file);
        }
    }
}
