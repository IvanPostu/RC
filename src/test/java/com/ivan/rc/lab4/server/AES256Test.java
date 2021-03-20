/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ivan
 */
public class AES256Test {
    
    public AES256Test() {
    }

    @Test
    public void testEncryptAndDecrypt() {
        AES256 cipher = new AES256();
        String key = "blaBla-BLA";
        String text = "Hello world !30943184u8";
        
        String encrypted = cipher.encrypt(text, key);
        String decrypted = cipher.decrypt(encrypted, key);
        
        Assertions.assertEquals(text, decrypted);
        
    }

    
}
