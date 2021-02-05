/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab1;

import java.io.File;
import java.util.List;

/**
 *
 * @author ivan
 */
public class Main {

    private static final String UTM_URL = "me.utm.md";

    public static void main(String[] args) {
        HttpClient httpClient = new HttpClient();

        try {
//            byte[] res = httpClient.doGetRequest(UTM_URL, "");
//            String html = new String(res);
//
//            List<String> imageLinks = new Utils()
//                    .extractImageLinksFromHtml(html, 
//                            new String[] {"png", "JPG", "gif"});

            FileWorker fw = new FileWorker();
            
//            

            byte[] img = httpClient
                    .doGetRequest(UTM_URL, "/img/europractice.png");
//            
//            fw.writeFile("europractice.png", img, true);
            char c = 'a';
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
