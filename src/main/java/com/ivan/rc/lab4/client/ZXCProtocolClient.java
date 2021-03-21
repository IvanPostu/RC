/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.Base64;

import javax.swing.JOptionPane;

import com.ivan.rc.lab4.common.ZXCProtocolDefinition;
import com.ivan.rc.lab4.server.AES256;
import com.ivan.rc.lab4.server.MainServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class ZXCProtocolClient {

    private static final Logger log = LogManager.getLogger(ZXCProtocolClient.class);

    private KeyPair keyPair;

    public void initSecuredConnection() {
        RSA rsa = new RSA();
        this.keyPair = rsa.generateKeyPair();

        if (getSymmetricKeyAndUserIdFromServer()) {
            JOptionPane.showMessageDialog(MainWindow.getInstance(), "Conexiune securizata initializata cu success",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            MainWindow.getInstance().jTextField1
                    .setText(String.valueOf(SecuredConnection.getInstance().getUserId().hashCode()));
        }
    }

    public String doRequest(ZXCProtocolDefinition.RequestType requestType, String action, String data) {
        StringBuilder requestStringBuilder = new StringBuilder();

        requestStringBuilder.append(String.format("Action: %s\n", action))
                .append(String.format("RequestType: %s\n", requestType.name()))
                .append(String.format("UserId: %s\n", SecuredConnection.getInstance().getUserId()));

        if (data != null) {
            AES256 aes = new AES256();
            String encryptedData = aes.encrypt(data, SecuredConnection.getInstance().getAesKey());
            requestStringBuilder.append(String.format("Data: %s\n", encryptedData));
        }

        try (Socket s = new Socket(InetAddress.getByName(MainServer.SERVER_HOST), MainServer.SERVER_SOCKET_PORT);
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                InputStream inStream = s.getInputStream()) {

            pw.print(requestStringBuilder.toString());
            pw.flush();

            byte[] response = getBytesFromInputStream(inStream);
            log.info(new String(response));

            String dataFromResponse = Arrays.stream(new String(response).split("\n")).filter(a -> a.contains("Data: "))
                    .findFirst().orElseThrow(() -> new RuntimeException()).split(": ")[1];

            return dataFromResponse;
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException(ex);
        }

    }

    private boolean getSymmetricKeyAndUserIdFromServer() {

        try (Socket s = new Socket(InetAddress.getByName(MainServer.SERVER_HOST), MainServer.SERVER_SOCKET_PORT);
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                InputStream inStream = s.getInputStream()) {

            pw.println(String.format("RequestType: %s",
                    ZXCProtocolDefinition.RequestType.ESTABLISH_SECURE_CONNECTION.name()));
            pw.println(String.format("PUBLIC_KEY: %s",
                    new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()))));
            pw.flush();

            byte[] result = getBytesFromInputStream(inStream);

            log.info(new String(result));
            String symmetricKey = Arrays.stream(new String(result).split("\n"))
                    .filter(a -> a.contains("SYMMETRIC_KEY: ")).findFirst()
                    .orElseThrow(() -> new RuntimeException("SYMMETRIC_KEY in header not found")).split(": ")[1];

            String userId = Arrays.stream(new String(result).split("\n")).filter(a -> a.contains("USER_ID: "))
                    .findFirst().orElseThrow(() -> new RuntimeException("USER_ID in header not found")).split(": ")[1];

            RSA rsa = new RSA();
            byte[] keyAESSymetric = rsa.decrypt(Base64.getDecoder().decode(symmetricKey), keyPair.getPrivate());
            String keyAES = new String(keyAESSymetric);

            SecuredConnection.getInstance().setAesKey(keyAES);
            SecuredConnection.getInstance().setUserId(userId);
            SecuredConnection.getInstance().setEstablished(true);
            log.info("Secured connection estabilshed for uId: {}", userId);
            log.info("Symetric key is: {}", keyAES);

            return true;
        } catch (Exception ex) {
            log.error(ex);
            throw new RuntimeException(ex);
        }

    }

    private byte[] getBytesFromInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int nRead;
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        byte[] result = buffer.toByteArray();
        buffer.flush();

        return result;
    }

}
