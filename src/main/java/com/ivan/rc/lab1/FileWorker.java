
package com.ivan.rc.lab1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileWorker {

    public void writeFile(String fileName, byte[] content, boolean overideIfExists)
            throws IOException {

        File tempDirectory = new File("downloads");
        File newFile = new File(tempDirectory.getAbsolutePath() + File.separator + fileName);

        if (newFile.exists()) {
            if (!overideIfExists) {
                return;
            } else {
                newFile.delete();
            }
        }

        newFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
            outputStream.write(content);
        }

    }

}
