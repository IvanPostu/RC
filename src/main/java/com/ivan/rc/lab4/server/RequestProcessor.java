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
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.ivan.rc.lab4.client.RSA;
import com.ivan.rc.lab4.common.ZXCProtocolDefinition;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ivan
 */
public class RequestProcessor implements Runnable {

    private static final Logger log = LogManager.getLogger(RequestProcessor.class);
    private static final Map<String, String> mapWithUserIdsAndSymmetricKeys = new HashMap<>();

    private Socket s;

    public RequestProcessor(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void processRequest() throws IOException {

        try (InputStream in = s.getInputStream()) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] dataBuffer = new byte[2048];
            int nRead;
            while (true) {
                nRead = in.read(dataBuffer, 0, dataBuffer.length);
                buffer.write(dataBuffer, 0, nRead);
                if (nRead != dataBuffer.length) {
                    break;
                }
            }

            byte[] result = buffer.toByteArray();
            buffer.flush();

            PrintWriter pw = new PrintWriter(s.getOutputStream());

            if (checkIfIsEstablishSecureConnection(new String(result))) {
                pw.print(establishSecureConnection(result));
                pw.flush();
                return;
            }

            String action = this.extractAction(result).orElse("");

            String uId = Arrays.stream(new String(result).split("\n")).filter(a -> a.contains("UserId: ")).findFirst()
                    .orElseThrow(() -> new RuntimeException()).split(": ")[1];

            String encryptedData = Arrays.stream(new String(result).split("\n")).filter(a -> a.contains("Data: "))
                    .findFirst().orElseThrow(() -> new RuntimeException()).split(": ")[1];

            String aesKey = mapWithUserIdsAndSymmetricKeys.get(uId);
            AES256 aes = new AES256();
            String plainData = aes.decrypt(encryptedData, aesKey);

            BusinessLogic.getInstance().processAction(action, plainData, uId, pw, aesKey);
            pw.write(
                    String.format("StatusResponseCode: %s\n", ZXCProtocolDefinition.StatusResponseCode.SUCCESS.name()));

            pw.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private boolean checkIfIsEstablishSecureConnection(String requestStr) {
        try {
            String reqType = Arrays.stream(requestStr.split("\n")).filter(a -> a.contains("RequestType: ")).findFirst()
                    .orElseThrow(() -> new RuntimeException()).split(": ")[1];

            return reqType.equals(ZXCProtocolDefinition.RequestType.ESTABLISH_SECURE_CONNECTION.name());
        } catch (Exception e) {
            return false;
        }

    }

    private String establishSecureConnection(byte[] requestBytes) throws Exception {
        StringBuilder sb = new StringBuilder();
        String pubKey = Arrays.stream(new String(requestBytes).split("\n")).filter(a -> a.contains("PUBLIC_KEY: "))
                .findFirst().orElseThrow(() -> new RuntimeException("PUBLIK_KEY in header not found")).split(": ")[1];
        byte[] pubKeyBytes = Base64.getDecoder().decode(pubKey);

        String symmetricKey = AES256.generateRandomKey(12);
        RSA rsa = new RSA();
        PublicKey pk = rsa.bytesToPublicKey(pubKeyBytes);
        String encryptedSymmetricKey = new String(Base64.getEncoder().encode(rsa.encrypt(symmetricKey, pk)));
        sb.append(String.format("StatusResponseCode: %s\n", ZXCProtocolDefinition.StatusResponseCode.SUCCESS.name()));
        sb.append(String.format("SYMMETRIC_KEY: %s\n", encryptedSymmetricKey));

        String userId = UUID.randomUUID().toString();
        sb.append(String.format("USER_ID: %s\n", userId));

        mapWithUserIdsAndSymmetricKeys.put(userId, symmetricKey);
        log.info("Secured connection estabilshed with success for uId: {}, hashcode: {}", userId, userId.hashCode());

        return sb.toString();
    }

    private Optional<String> extractAction(byte[] requestBytes) {
        try {
            String action = Arrays.stream(new String(requestBytes).split("\n")).filter(a -> a.contains("Action: "))
                    .findFirst().orElse(null).split(": ")[1];

            return Optional.ofNullable(action);
        } catch (Exception e) {
            log.error("IN extractAction: Action not found");
            return Optional.ofNullable(null);
        }
    }

}
