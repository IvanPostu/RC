/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ivan
 */
public class FileWorker {

    public void writeFile(String fileName,
            byte[] content,
            boolean overideIfExists) throws IOException {

        File tempDirectory = new File("downloads");
        File newFile = new File(tempDirectory
                .getAbsolutePath() + File.separator + fileName);

        if(newFile.exists() && !overideIfExists){
            return;
        }
        
        newFile.createNewFile();
        
        try (FileOutputStream outputStream = new FileOutputStream(newFile)) {
            outputStream.write(content);
        }
        
        
    }

}
