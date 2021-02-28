/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivan.rc.lab2;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

/**
 *
 * @author ivan
 */
public class EmailWorker {

    public Message[] fetchMessages() {

        Session session = Session.getDefaultInstance(new Properties());

        try {
            Store store = session.getStore("imaps");
            store.connect("imap.googlemail.com", 993, LoginState.email, LoginState.password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("FEtch messages with success");
            return messages;
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public void sendMail(String recipient, final String title, final String text, List<File> attachmentFiles) {
        sendMailInternal(recipient, title, text, attachmentFiles);
    }

    private void sendMailInternal(String recipient, final String title, final String text, List<File> attachmentFiles) {
        try {

            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.socketFactory.port", "587");
            properties.put("mail.smtp.socketFactory.fallback", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.starttls.required", "true");
            properties.put("mail.smtp.ssl.enable", "false");

            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(LoginState.email, LoginState.password);
                }
            });

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(LoginState.email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(title);
            message.setText(text);

            MimeMultipart mimeMultipart = new MimeMultipart();
            for (File f : attachmentFiles) {
                MimeBodyPart bodyPart = new MimeBodyPart();
                DataSource dataSource = new FileDataSource(f);
                bodyPart.setDataHandler(new DataHandler(dataSource));
                bodyPart.setFileName(f.getName());
                mimeMultipart.addBodyPart(bodyPart);
            }

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(text, "text/html");
            mimeMultipart.addBodyPart(bodyPart);

            message.setContent(mimeMultipart);

            Transport.send(message);
        } catch (MessagingException ex) {
            System.out.println(ex);
        }

    }

}
