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

    private final ExecutorService threadPool;
    private final Semaphore semaphore;

    private static final DownloadWorker DOWNLOAD_WORKER = new DownloadWorker();

    public static DownloadWorker getInstance() {
        return DOWNLOAD_WORKER;
    }

    private DownloadWorker() {
        this.threadPool = Executors.newFixedThreadPool(4);
        this.semaphore = new Semaphore(2);
    }

    public void run(String websiteUrl, List<String> downloadUrls) {
        List<String> threadSafeDownloadUrls = Collections.synchronizedList(downloadUrls);

        threadPool.submit(new DownloadRunnable(threadSafeDownloadUrls, websiteUrl, semaphore));
        threadPool.submit(new DownloadRunnable(threadSafeDownloadUrls, websiteUrl, semaphore));
        threadPool.submit(new DownloadRunnable(threadSafeDownloadUrls, websiteUrl, semaphore));
        threadPool.submit(new DownloadRunnable(threadSafeDownloadUrls, websiteUrl, semaphore));
    }

    public void shutDown() {
        threadPool.shutdown();
    }

    private class DownloadRunnable implements Runnable {

        private List<String> downloadUrls;
        private String websiteUrl;
        private FileWorker fileWorker;
        private Semaphore semaphore;

        DownloadRunnable(List<String> downloadUrls, String websiteUrl, Semaphore semaphore) {
            this.downloadUrls = downloadUrls;
            this.websiteUrl = websiteUrl;
            this.fileWorker = new FileWorker();
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (!downloadUrls.isEmpty()) {
                String link = downloadUrls.remove(0);
                String filename = link.replace('/', '_');

                try {

                    LOGGER.info("NETWORK: Start downloading {} file.", filename);
                    HttpClient httpClient = new CustomHttpClientImpl(websiteUrl, link);
                    httpClient.doGetRequest();
                    byte[] imgData = httpClient.getBody();
                    LOGGER.info("NETWORK: Finish downloading {} file.", link);


                    semaphore.acquire();
                    LOGGER.info("FILESYSTEM: Start save to disk {} file, semaphore count={}",
                            filename, semaphore.availablePermits());
                    fileWorker.writeFile(filename, imgData, true);
                    semaphore.release();

                    LOGGER.info("FILESYSTEM: Finish save to disk {} file, semaphore count={}",
                            filename, semaphore.availablePermits());
                } catch (IOException | InterruptedException | RuntimeException e) {
                    LOGGER.error(e);

                }
            }
        }

    };

}
