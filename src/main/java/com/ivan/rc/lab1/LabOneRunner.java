/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab1;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ivan
 */
public class LabOneRunner {

    private static final String UTM_URL = "me.utm.md";

    public static void run() {
        HttpClient httpClient = new HttpClient();

        try {
//            byte[] res = httpClient.doGetRequest(UTM_URL, "/");
//            String html = new String(res);
//
//            List<String> imageLinks = new Utils()
//                    .extractImageLinksFromHtml(html, 
//                            new String[] {"png", "JPG", "gif"});

            FileWorker fw = new FileWorker();

            byte[] imgResponse = httpClient
                    .doGetRequest(UTM_URL, "/img/europractice.png");
            byte[] imgData = httpClient.getBody(imgResponse);
            fw.writeFile("zeuropractice.png", imgData, true);
        } catch (IOException e) {
        }
    }

}
