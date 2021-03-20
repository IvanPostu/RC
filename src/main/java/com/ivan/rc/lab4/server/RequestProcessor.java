/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import com.ivan.rc.lab4.common.ZXCProtocolDefinition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class RequestProcessor implements Runnable {

    private static final Logger log = LogManager.getLogger(RequestProcessor.class);

    private Socket s;

    public RequestProcessor(Socket s) {
        this.s = s;
    }

    private void processRequest() throws IOException {

        try (InputStream in = s.getInputStream()) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int nRead;
            while (true) {
                nRead = in.read(data, 0, data.length);
                buffer.write(data, 0, nRead);
                if (nRead != data.length) {
                    break;
                }
            }

            byte[] result = buffer.toByteArray();
            buffer.flush();

            String pubKey = Arrays.stream(new String(result).split("\n")).filter(a -> a.contains("PUBLIC_KEY: "))
                    .findFirst().orElseThrow(() -> new RuntimeException("PUBLIK_KEY in header not found"))
                    .split(": ")[1];
            byte[] pubKeyBytes = pubKey.getBytes();

            String symmetricKey = AES256.generateRandomKey(12);

            // byte[] cipherTextArray = encrypt(symmetricKey, publicKey);
            // String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);

            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println(String.format("StatusResponseCode: %s", ZXCProtocolDefinition.StatusResponseCode.SUCCESS));
            // pw.println(String.format("SYMMETRIC_KEY: %s",
            // new String(Base64.getEncoder().encode(publicKey.getEncoded()))));

            // pr.println("Yes *");
            pw.flush();

        }

    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
