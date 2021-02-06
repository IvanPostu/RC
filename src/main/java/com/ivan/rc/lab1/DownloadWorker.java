package com.ivan.rc.lab1;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class DownloadWorker {

    private static final Logger LOGGER = LogManager.getLogger(DownloadWorker.class);
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4);
    private static final Semaphore SEMAPHORE = new Semaphore(2);

    private final List<String> downloadUrls;
    private final HttpClient httpClient;
    private final String websiteUrl;

    public DownloadWorker(List<String> downloadUrls, HttpClient httpClient, String websiteUrl) {
        this.downloadUrls = Collections.synchronizedList(downloadUrls);
        this.httpClient = httpClient;
        this.websiteUrl = websiteUrl;
    }

    public void run() {
        THREAD_POOL.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));
        THREAD_POOL.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));
        THREAD_POOL.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));
        THREAD_POOL.submit(new MyRunnable(downloadUrls, httpClient, websiteUrl));

        THREAD_POOL.shutdown();

    }

    private class MyRunnable implements Runnable {

        private List<String> downloadUrls;
        private HttpClient httpClient;
        private String websiteUrl;
        private FileWorker fileWorker;

        MyRunnable(List<String> downloadUrls, HttpClient httpClient, String websiteUrl) {
            this.downloadUrls = downloadUrls;
            this.httpClient = httpClient;
            this.websiteUrl = websiteUrl;
            this.fileWorker = new FileWorker();
        }

        @Override
        public void run() {
            while (!downloadUrls.isEmpty()) {
                String link = downloadUrls.remove(0);
                String filename = link.replace('/', '_');

                try {

                    LOGGER.info("NETWORK: Start downloading {} file.", filename);

                    byte[] imgResponse = httpClient.doGetRequest(websiteUrl, link)
                            .orElseThrow(() -> new RuntimeException());

                    LOGGER.info("NETWORK: Finish downloading {} file.", link);

                    byte[] imgData = httpClient.getBody(imgResponse);

                    SEMAPHORE.acquire();

                    LOGGER.info("FILESYSTEM: Start save to disk {} file, semaphore count={}",
                            filename, SEMAPHORE.availablePermits());

                    fileWorker.writeFile(filename, imgData, true);

                    SEMAPHORE.release();

                    LOGGER.info("FILESYSTEM: Finish save to disk {} file, semaphore count={}",
                            filename, SEMAPHORE.availablePermits());



                } catch (IOException | InterruptedException | RuntimeException e) {
                    LOGGER.error("Error while downloading file: {}", link);

                }
            }
        }

    };

}
