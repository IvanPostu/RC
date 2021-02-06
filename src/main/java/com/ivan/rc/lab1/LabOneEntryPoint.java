package com.ivan.rc.lab1;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class LabOneEntryPoint {

    private static final String[] URLS = new String[] {"http://me.utm.md", "https://utm.md"};
    private static final String[] IMAGE_EXTENSIONS = new String[] {"png", "JPG", "gif"};
    private static final Logger logger = LogManager.getLogger(LabOneEntryPoint.class);

    private String doRequestAndExtractHtml(String url) {
        HttpClient httpClient = new CustomHttpClientImpl(url, "");
        httpClient.doGetRequest();

        byte[] res = httpClient.getBody();
        String html = new String(res);
        logger.info("HTML document for website: {} downloaded with success.", url);

        return html;
    }

    public void run() {


        for (String url : URLS) {
            String html = doRequestAndExtractHtml(url);

            List<String> imageLinks = ImageLinkExtractor.extractImageRelativeLinksFromHtml(html,
                    IMAGE_EXTENSIONS, url);

            DownloadWorker downloadWorker = new DownloadWorker(imageLinks, url);
            downloadWorker.run();
        }

        // logger.info("Program finished!!!");
    }

}
