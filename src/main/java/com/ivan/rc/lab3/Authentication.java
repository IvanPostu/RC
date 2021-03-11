/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab3;

/**
 *
 * @author ivan
 */
public class Authentication {
    private String cookie;
    private boolean authenticated;

    private static Authentication auth = new Authentication("", false);
    
    public static Authentication getInstance(){
        return auth;
    }
    
    private Authentication(String cookie, boolean authenticated) {
        this.cookie = cookie;
        this.authenticated = authenticated;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
    
    
}
