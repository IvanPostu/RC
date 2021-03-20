/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Map;

import com.ivan.rc.lab4.client.RSA;
import com.ivan.rc.lab4.server.MainServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class ZXCProtocolClient {

    private static final Logger log = LogManager.getLogger(ZXCProtocolClient.class);

    private String body;
    private ZXCProtocolDefinition.RequestType requestType;
    private KeyPair keyPair;

    public void doRequest(ZXCProtocolDefinition.RequestType requestType, String body, Map<String, String> header) {

        RSA rsa = new RSA();
        this.keyPair = rsa.generateKeyPair();

        String z = getSymmetricKeyFromServer();

    }

    private String getSymmetricKeyFromServer() {

        try (Socket s = new Socket(InetAddress.getByName(MainServer.SERVER_HOST), MainServer.SERVER_SOCKET_PORT);
                PrintWriter pw = new PrintWriter(s.getOutputStream())) {

            pw.println(String.format("RequestType: %s",
                    ZXCProtocolDefinition.RequestType.ESTABLISH_SECURE_CONNECTION.name()));
            pw.println(String.format("PUBLIC_KEY: %s",
                    new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()))));
            pw.flush();

            InputStream inStream = s.getInputStream();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[2048];
            int nRead;
            while ((nRead = inStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            byte[] result = buffer.toByteArray();
            buffer.flush();

            log.info(new String(result));

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return "";
    }

}
