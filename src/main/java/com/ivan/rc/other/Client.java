package com.ivan.rc.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        try (Socket s = new Socket("localhost", 8080)) {

            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("It's working!!!1");
            pw.println("It's working!!!2");
            pw.println("It's working!!!3");
            pw.println("It's working!!!4");
            pw.println("\nEND\n");
            pw.flush();

            InputStreamReader sr = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(sr);

            String str = bf.readLine();
            System.out.println("Server: " + str);

        }

    }
}
