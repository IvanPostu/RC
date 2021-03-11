/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.misc.IOUtils;

/**
 *
 * @author ivan
 */
public class ChatPanel extends javax.swing.JPanel {

    private static final Logger logger = LogManager.getLogger(ChatPanel.class);
    
    /**
     * url -> category name
     */
    private  Map<String, String> categories;

    /**
     * Creates new form LoginPanel
     */
    public ChatPanel() {
        initComponents();
        categories = new LinkedHashMap<String, String>();
        categories.put( "Поговорим", "r1/");
        categories.put( "Новый год", "r14/");
        categories.put( "Знакомства", "r12/");
        categories.put( "Для новичков", "r8/");
        categories.put( "Бeспредел", "r6/");
        categories.put( "18+", "r7/");

        final DefaultListModel model = new DefaultListModel();
        
        categories.forEach((k, v) -> {
            model.addElement(new Object(){
                public String toString(){
                    return k;
                }
            });
        });
        
        jList1.setModel(model);
        
        
    }

    @Override
    public void addNotify() {
        super.addNotify();

    }

    @Override
    public void removeNotify() {
        super.removeNotify();

    }



    private String fetchMessages(String sufixUrl) {

        try (CloseableHttpClient httpclient = HttpClients
                .custom()
                .build()) {

            HttpUriRequest request = RequestBuilder.get()
                    .setUri(String.format("http://chat.chateg.ru/%s", sufixUrl))
                    .setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                    .setHeader("Cookie", Authentication.getInstance().getCookie())
                    .setConfig(ConfigWithProxyFactory.getConfig())
                    .build();

            logger.info("Executing request " + request.getRequestLine());

            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String strData = "";
                    if (entity != null) {
                        byte[] data = IOUtils.readAllBytes(entity.getContent());
                        strData = new String(data, StandardCharsets.UTF_8);
                        return strData;

                    } else {
                        throw new RuntimeException("Entity is null");
                    }
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            return httpclient.execute(request, responseHandler);

        } catch (IOException ex) {
            logger.error(ex);
            return "";
        }

    }
    
    private List<String> findChatMessagesUsingRegexp(String html){
        List<String> messages = new LinkedList<>();
        List<String> usernames = new LinkedList<>();
        
        String regexp = "<\\s*span[^>]*class=\"u\"*>(.*?)<\\s*/\\s*span>";

        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(html);

        while (m.find()) {
            String username = m.group(1);
            usernames.add(username);
        }

        regexp = "<\\s*span[^>]*class=\"text-[0-9] text-s[0-9]\"*>(.*?)<\\s*/\\s*span>";
        p = Pattern.compile(regexp);
        m = p.matcher(html);

        while (m.find()) {
            String messageText = m.group(1);
            messageText = messageText.replaceAll(
                    "<\\s*img[^>]*/>", "");
            messages.add(messageText);
        }
        
        List<String> smaller = messages.size() > usernames.size() ? usernames : messages;
        List<String> result = new ArrayList<>(smaller.size());
        
        for (int i = 0; i < smaller.size(); i++) {
            result.add(String.format("%s:   %s\n\n", usernames.get(i), messages.get(i)));
        }
        
        return result;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(800, 430));

        jScrollPane1.setViewportView(jList1);

        jLabel3.setText("Chat categories");

        jButton2.setText("Fetch messages");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel1.setText("Messages");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!Authentication.getInstance().isAuthenticated()) {
            JOptionPane.showMessageDialog(this.getParent(),
                    "Este necesar de autentificat", "Error auth", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int index = jList1.getSelectedIndex();
        
        if(index == -1) return;
        
        String selectedCategory = ((Object)jList1.getSelectedValue()).toString();
        String selectedCategoryUrl = categories.get(selectedCategory);

        String html = fetchMessages(selectedCategoryUrl);
        List<String> usersWithMessages = findChatMessagesUsingRegexp(html);
        String bigMessage = usersWithMessages
                .stream()
                .collect(Collector.of(() -> new StringBuilder(), (acc, cur) -> {
                    acc.append(cur);
                }, (sb1, sb2) -> {
                    return sb1.append(sb2);
                })).toString();

        jTextArea1.setText(bigMessage);
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
