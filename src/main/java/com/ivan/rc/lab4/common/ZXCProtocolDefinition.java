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
        ESTABLISH_SECURE_CONNECTION, SIMPLE, EXTENDED
    }

    public static enum StatusResponseCode {
        SUCCESS, ERROR
    }

}
