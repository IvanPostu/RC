package com.ivan.rc.lab1;

public interface HttpClient {

    void doGetRequest();

    boolean successRequest();

    byte[] getHead();

    byte[] getBody();
}
