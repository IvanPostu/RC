/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab1;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author ivan
 */
public class HttpClient {

    private static final int PORT = 80;

    public byte[] doGetRequest(String host, String relativePath){


        try (Socket socket = new Socket(InetAddress.getByName(host ), PORT)) {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.print("GET "+relativePath+" HTTP/1.1\r\n");
            pw.print("User-Agent: Simple Http Client\r\n");
            pw.print(String.format("Host: " + host  + ":80\r\n\r\n"));
            pw.flush();

            InputStream input = socket.getInputStream();
            InputStreamReader reader = new InputStreamReader(input);

            int character;
            StringBuilder data = new StringBuilder();

            while ((character = reader.read()) != -1) {
                data.append((char) character);
            }

            return data.toString().getBytes();
        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }

        return null;
    }

}
