package com.ivan.rc.lab1;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class LabOneEntryPoint {

    private static final String UTM_URL = "me.utm.md";
    private static final Logger logger = LogManager.getLogger(LabOneEntryPoint.class);

    private String extractHtmlFromUrl(String url, HttpClient httpClient) {
        byte[] res = httpClient.doGetRequest(url, "/").orElseThrow(() -> {
            String errMsg = "Error on request ";
            return new RuntimeException(errMsg);
        });

        String html = new String(res);

        logger.info("HTML downloaded with success.");

        return html;
    }

    public void run() {
        HttpClient httpClient = new HttpClient();
        String html = extractHtmlFromUrl(UTM_URL, httpClient);

        List<String> imageLinks =
                new Utils().extractImageLinksFromHtml(html, new String[] {"png", "JPG", "gif"});

        DownloadWorker downloadWorker = new DownloadWorker(imageLinks, httpClient, UTM_URL);
        downloadWorker.run();

    }

}
