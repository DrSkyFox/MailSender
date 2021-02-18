package com.mailsender;

import com.mailsender.settings.MailToTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;


public class MailMessage {

    private MailSession session;
    private List<MailToTask> tasks;
    private static final Logger logger = LogManager.getLogger(MailMessage.class.getName());

    public MailMessage(ConfigInit configInit) {
        session = configInit.readConfigFileAndGetSession();
        tasks = configInit.readConfigFileAndGetTask();
    }

    public void doAllTask() {
        MailToTask task = null;
        for (int i = 0; i < tasks.size(); i++) {
            task = tasks.get(i);
            logger.info("start task : " + task.toString());
            sendEmail(task.getFromEmail(), task.getSendListToEmail(), task.getSubject(), task.getText(), task.getAttachedFilesInDirectory());
        }
    }

    private void sendEmail(String sendFromEmail, List<String> sendToEmail, String subject, String text, List<String> files) {

        Message message = new MimeMessage(session.getSession());
        logger.info("Session got");
        try {
            logger.info("Settings from Email :" + sendFromEmail);
            message.setFrom(new InternetAddress(sendFromEmail));
        } catch (MessagingException e) {
            logger.error("Cant set parameter: fromEmail", e);
        }

        for (String s : sendToEmail
        ) {
            try {
                logger.info("Settings to Email :" + s);
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(s));
            } catch (MessagingException e) {
                e.printStackTrace();
                logger.error( "Cant set parameter: toEmails", e);
            }
        }


        try {

            if (sendFromEmail != null) {
                logger.info("Settings Subject of Mail");
                message.setSubject(subject);
            } else {
                message.setSubject("Subject");
            }

            Multipart multipart = new MimeMultipart();
            BodyPart part = getMessagePart(text);
            multipart.addBodyPart(part);

            logger.info("Attaching files to Mail");
            for (String file : files
            ) {
                multipart.addBodyPart(getAttachFilePart(file));
            }

            message.setContent(multipart);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error( "SomeThing gonna wrong", e);
        }

        try {
            logger.info("SendingMessage");
            Transport.send(message);
        } catch (MessagingException e) {
            logger.error( "Cant send Email", e);
        }
    }

    private BodyPart getMessagePart(String text) throws MessagingException {
        logger.info("Set message body: " + text);
        BodyPart part = new MimeBodyPart();

        if (text != null) {
            part.setText(text);
        } else {
            part.setFileName("Text");
        }
        return part;
    }

    private BodyPart getAttachFilePart(String file) {
        BodyPart part = new MimeBodyPart();
        try {
            logger.info(String.format("Attaching file: %s", file));
            part.setDataHandler(new DataHandler(new FileDataSource(file)));
        } catch (MessagingException e) {
            logger.error( String.format("Cant attach file: %s", file), e);
        }
        try {
            logger.info("Setting filename: " + file);
            part.setFileName(file);
        } catch (MessagingException e) {
            logger.error( String.format("Cant set name in message body. File: %s", file), e);
        }
        return part;
    }


}
