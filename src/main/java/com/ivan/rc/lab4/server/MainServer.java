/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ivan.rc.configuration.Log4jConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class MainServer {

    static {
        Log4jConfiguration.configure();
    }

    private static final Logger log = LogManager.getLogger(MainServer.class);

    private final ExecutorService threadPool;
    private static final int THREAD_POOL_SIZE = 4;

    public static final int SERVER_SOCKET_PORT = 8080;
    public static final String SERVER_HOST = "localhost";

    private MainServer() throws IOException {
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    private void run() throws IOException {
        log.info("Server application listen host: {} and port: {}", SERVER_HOST, SERVER_SOCKET_PORT);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_SOCKET_PORT)) {
            while (true) {
                Socket s = serverSocket.accept();
                Runnable r = new RequestProcessor(s);
                threadPool.submit(r);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new MainServer().run();
    }
}
