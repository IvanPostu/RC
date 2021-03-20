package com.ivan.rc.lab4.client;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {

    public static final String ALGORITHM_NAME = "RSA";
    
    public PublicKey bytesToPublicKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
        PublicKey publicKey2 = keyFactory.generatePublic(publicKeySpec);

        return publicKey2;
    }

    public PrivateKey bytesToPrivateKey(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytes);
        PrivateKey privateKey2 = keyFactory.generatePrivate(privateKeySpec);

        return privateKey2;
    }

    public KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        keyPairGenerator.initialize(4096);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        return keyPair;

    }

    public byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        return cipherText;
    }

    public byte[] decrypt(byte[] cipherTextArray, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);

        return decryptedTextArray;
    }
}
