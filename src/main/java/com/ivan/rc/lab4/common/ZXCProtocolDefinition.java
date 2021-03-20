/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab4.common;

/**
 *
 * @author ivan
 */
public class ZXCProtocolDefinition {
    
    public static enum RequestType {
        ESTABLISH_SECURE_CONNECTION,
        SIMPLE,
        EXTENDED
    }

    public static enum StatusResponseCode {
        SUCCESS(0),
        ERROR(1),
        INVALID_DATA(2),
        HELLO_SUCCESS(3),
        HELLO_ERROR(4),
        CREATED(5),
        UPDATED(6),
        DELETED(7);

        private final int value;

        private StatusResponseCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

  
}
