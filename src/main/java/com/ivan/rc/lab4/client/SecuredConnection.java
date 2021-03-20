package com.ivan.rc.lab4.client;

public class SecuredConnection {
    private boolean isEstablished = false;
    private String aesKey = "";
    private String userId = "";

    private SecuredConnection() {

    }

    private static SecuredConnection instance = new SecuredConnection();

    public static SecuredConnection getInstance() {
        return instance;
    }

    public boolean isEstablished() {
        return isEstablished;
    }

    public void setEstablished(boolean isEstablished) {
        this.isEstablished = isEstablished;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
