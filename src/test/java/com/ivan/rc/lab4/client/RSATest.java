/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.client;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ivan
 */
public class RSATest {

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        RSA rsa = new RSA();
        KeyPair kp = rsa.generateKeyPair();
        
        final String plainText = "AAA bb afh akfha kfdf.";
        
         // Encryption
        byte[] cipherTextArray = rsa.encrypt(plainText, kp.getPublic());
        String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
        
        // Decryption
        byte[] decryptedText = rsa.decrypt(cipherTextArray, kp.getPrivate());
        Assertions.assertEquals(plainText, new String(decryptedText));
        
    }
    
    @Test
    public void testBytesToPublicKey() throws Exception{
        RSA rsa = new RSA();
        KeyPair kp = rsa.generateKeyPair();
        
        byte[] pubKeyBytes = kp.getPublic().getEncoded();
        
        PublicKey pk = rsa.bytesToPublicKey(pubKeyBytes);
        Assertions.assertEquals(kp.getPublic(), pk);
    }
    
        @Test
    public void testBytesToPrivateKey() throws Exception{
        RSA rsa = new RSA();
        KeyPair kp = rsa.generateKeyPair();
        
        byte[] privateKeyBytes = kp.getPrivate().getEncoded();
        
        PrivateKey pk = rsa.bytesToPrivateKey(privateKeyBytes);
        Assertions.assertEquals(kp.getPrivate(), pk);
    }

}
