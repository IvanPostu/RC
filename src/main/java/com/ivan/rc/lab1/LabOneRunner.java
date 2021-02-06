package com.ivan.rc.lab1;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class LabOneRunner {

    private static final String UTM_URL = "me.utm.md";
    private static final Logger logger = LogManager
            .getLogger(LabOneRunner.class);

    public void run() {
        HttpClient httpClient = new HttpClient();

        try {
            byte[] res = httpClient
                    .doGetRequest(UTM_URL, "/")
                    .orElseThrow(() -> new Exception());

            String html = new String(res);

            List<String> imageLinks = new Utils()
                    .extractImageLinksFromHtml(html,
                            new String[]{"png", "JPG", "gif"});

            FileWorker fw = new FileWorker();

            Iterator<String> imageLinksIterator = imageLinks.iterator();

            while (imageLinksIterator.hasNext()) {
                String s = imageLinksIterator.next();
                String requestUrl = "/" + s;
                try {
                    logger.info("Start file download {}", requestUrl);
                    byte[] imgResponse = httpClient
                            .doGetRequest(UTM_URL, requestUrl)
                            .orElseThrow(() -> new RuntimeException());

                    byte[] imgData = httpClient.getBody(imgResponse);
                    fw.writeFile(s.replace('/', '_'), imgData, true);
                    logger.info("Success file download {}", requestUrl);
                } catch (RuntimeException e) {
                    logger.error("Error while downloading file {}", requestUrl);
                }

            }

        } catch (IOException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
