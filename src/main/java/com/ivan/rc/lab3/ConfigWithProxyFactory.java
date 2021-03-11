/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab3;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;

/**
 *
 * @author ivan
 */
public class ConfigWithProxyFactory {

    public static RequestConfig getConfig() {

        HttpHost proxy = new HttpHost("136.233.215.136", 80);

        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();

        return config;
    }
}
