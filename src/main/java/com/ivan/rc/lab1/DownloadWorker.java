package com.ivan.rc.lab1;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class DownloadWorker {

    private static final Logger logger = LogManager.getLogger(DownloadWorker.class);
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(4);

    private List<String> downloadUrls;
    private HttpClient httpClient;
    private String websiteUrl;

    public DownloadWorker(List<String> downloadUrls, HttpClient httpClient, String websiteUrl) {
        this.downloadUrls = Collections.synchronizedList(downloadUrls);
        this.httpClient = httpClient;
        this.websiteUrl = websiteUrl;
    }

    public void run() {
        threadPool.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));
        threadPool.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));
        threadPool.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));
        threadPool.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));

        threadPool.shutdown();

    }

    private class MyRunnable implements Runnable {

        private List<String> downloadUrls;
        private HttpClient httpClient;
        private String websiteUrl;

        MyRunnable(List<String> downloadUrls, HttpClient httpClient, String websiteUrl) {
            this.downloadUrls = downloadUrls;
            this.httpClient = httpClient;
            this.websiteUrl = websiteUrl;
        }

        @Override
        public void run() {
            while (!downloadUrls.isEmpty()) {
                try {
                    String link = '/' + downloadUrls.remove(0);
                    FileWorker fw = new FileWorker();

                    try {
                        logger.info("Start file download: {}", link);
                        byte[] imgResponse = httpClient.doGetRequest(websiteUrl, link)
                                .orElseThrow(() -> new RuntimeException());

                        byte[] imgData = httpClient.getBody(imgResponse);
                        fw.writeFile(link.replace('/', '_'), imgData, true);
                        logger.info("Success file download: {}", link);
                    } catch (RuntimeException e) {
                        logger.error("Error while downloading file: {}", link);
                    }
                } catch (IndexOutOfBoundsException e) {
                } catch (IOException e) {
                }
            }
        }

    };

}
