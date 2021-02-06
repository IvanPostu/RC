/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public byte[] doGetRequest(String host, String relativePath) {

        try (Socket socket = new Socket(InetAddress.getByName(host), PORT)) {

            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.print("GET " + relativePath + " HTTP/1.1\r\n");
            pw.print("User-Agent: Simple Http Client\r\n");
            pw.print(String.format("Host: " + host + ":80\r\n\r\n"));
            pw.flush();

            InputStream inStream = socket.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int nRead;

            while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            byte[] result = buffer.toByteArray();
            buffer.flush();

            return result;
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }

        return null;
    }

    public byte[] getBody(byte[] data) {
        int count = data.length;

        try {
            int i = 0;
            for (i = 0; i < data.length; i++, count--) {
                if (data[i] == (byte) '\r'
                        && data[i + 1] == (byte) '\n'
                        && data[i + 2] == (byte) '\r'
                        && data[i + 3] == (byte) '\n') {
                    count -= 4;
                    i += 4;
                    break;
                }

            }

            char c = 'a';
            byte[] result = new byte[count];

            for (int j = 0; j < result.length; j++) {
                result[j] = data[j + i];
            }

            return result;

        } catch (IndexOutOfBoundsException e) {
        }

        return data;
    }

}
