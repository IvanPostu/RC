package com.ivan.rc.other;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8080);
        Socket s = ss.accept();

        System.out.println("Client connected");

        InputStream in = s.getInputStream();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int nRead;

        while (true) {
            nRead = in.read(data, 0, data.length);
            buffer.write(data, 0, nRead);
            if (nRead < data.length) {
                break;
            }
        }

        byte[] result = buffer.toByteArray();
        buffer.flush();

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("Yes *\n");
        pr.flush();

        ss.close();
    }
}
