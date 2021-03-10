/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Timer;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.IOUtils;

/**
 *
 * @author ivan
 */
public class LoginPanel extends javax.swing.JPanel {

    private static final Logger logger = LogManager.getLogger(LoginPanel.class);

    private boolean isFetch = false;
    private int timerProgress = 0;
    private Thread requestThread;

    private Timer t = new Timer(100, (e) -> this.onTimerTick());

    /**
     * Creates new form LoginPanel
     */
    public LoginPanel() {
        initComponents();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.t.start();
        requestThread = new Thread(() -> {
            this.isFetch = true;
            String preLoginCookie = preLoginRequest();
            loginRequest(loginField.getText(), passwordField.getText(), preLoginCookie);
            this.isFetch = false;
        });
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        this.t.stop();
        try {
            requestThread.join();
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
    }

    private void onTimerTick() {
        timerProgress = timerProgress >= 100 ? 0 : timerProgress + 10;
        this.jProgressBar1.setValue(isFetch ? timerProgress : 0);
    }

    private String findCookieForLoginRequest(String html) {
        String cookieRegexp = "<a\\s+[^>]*href=\"([^\"]*)\"[^>]*>";

        Pattern p = Pattern.compile(cookieRegexp);
        Matcher m = p.matcher(html);

        while (m.find()) {

            String href = m.group(1);
            if (href.contains("PHPSESSID")) {
                String[] strs = href.split("PHPSESSID=");
                return strs[1];
            }

        }

        return "";
    }

    private String preLoginRequest() {
        String loginCookie = "";
        try (CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client
                        .execute(new HttpGet("http://club.chateg.ru"))) {
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                byte[] data = IOUtils.readAllBytes(entity.getContent());
                String strData = new String(data, StandardCharsets.UTF_8);
                loginCookie = findCookieForLoginRequest(strData);

                if (loginCookie.length() > 0) {
                    logger.info("Pre login cookie fetched with success {}", loginCookie);
                } else {
                    throw new RuntimeException("Pre login cookie not Cookie fetched with success");
                }

            } else {
                throw new RuntimeException("Entity is null");
            }

        } catch (Exception e) {
            logger.error(e);
        }

        return loginCookie;
    }

    private void loginRequest(String username, String password, String preLoginCookie) {

        try (CloseableHttpClient httpclient = HttpClients
                .custom()
                .build()) {

            String body = String.format("login=%s&pass=%s&action=login", username, password);
            StringEntity stringEntity = new StringEntity(body);
            
            HttpUriRequest request = RequestBuilder.post()
                    .setEntity(stringEntity)
                    .setUri("http://club.chateg.ru/index.php")
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .setHeader("Cookie", String.format("PHPSESSID=%s", preLoginCookie))
                    .build();

            logger.info("Executing request " + request.getRequestLine());

            ResponseHandler<Integer> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status == 302) {
                    
                    
                    Header cookie = response.getLastHeader("Set-Cookie");
                    logger.info("Success login request {}", cookie); 
                      
                    Authentication.getInstance().setCookie(cookie.getValue());
                    Authentication.getInstance().setAuthenticated(true);
                    return 1;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            
            httpclient.execute(request, responseHandler);

        } catch (IOException ex) {
            logger.error(ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();

        setPreferredSize(new java.awt.Dimension(800, 430));

        jLabel1.setText("E-mail:");

        jLabel2.setText("Password:");

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(loginField)
                            .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
                .addContainerGap(331, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginField, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(passwordField))
                .addGap(47, 47, 47)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (requestThread.isAlive()) {
            return;
        }

        requestThread.run();

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField loginField;
    private javax.swing.JPasswordField passwordField;
    // End of variables declaration//GEN-END:variables
}
