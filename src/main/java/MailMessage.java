import com.sun.jdi.request.ClassUnloadRequest;
import com.task.MailSendTasks;
import com.task.MailToTask;

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
    private MailSendTasks tasks;
    private LogWriter logWriter;

    public MailMessage(ConfigInit configInit, LogWriter logWriter) {
        this.logWriter = logWriter;
        session = configInit.readConfigFileAndGetSession();
        tasks = configInit.readConfigFileAndGetTask();
    }

    public void doAllTask() {
        MailToTask task = null;
        for (int i = 0; i < tasks.getMailToTaskList().size(); i++) {
            task = tasks.getMailToTaskList().get(i);
            logWriter.logInfo("start task : " + task.toString());
            sendEmail(task.getFromEmail(), task.getSendListToEmail(), task.getSubject(), task.getText(), task.getAttachedFilesInDirectory());
        }
    }

    private void sendEmail(String sendFromEmail, List<String> sendToEmail, String subject, String text, List<String> files) {

        Message message = new MimeMessage(session.getSession());
        logWriter.logInfo("Session got");
        try {
            logWriter.logInfo("Settings from Email :" + sendFromEmail);
            message.setFrom(new InternetAddress(sendFromEmail));
        } catch (MessagingException e) {
            logWriter.logWarning("Exception: ", e);
        }

        for (String s : sendToEmail
        ) {
            try {
                logWriter.logInfo("Settings to Email :" + s);
                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(s));
            } catch (MessagingException e) {
                e.printStackTrace();
                logWriter.logWarning("Exception: ", e);
            }
        }


        try {

            if (sendFromEmail != null) {
                logWriter.logInfo("Settings Subject of Mail");
                message.setSubject(subject);
            } else {
                message.setSubject("Subject");
            }

            Multipart multipart = new MimeMultipart();
            BodyPart part = getMessagePart(text);
            multipart.addBodyPart(part);

            logWriter.logInfo("Attaching files to Mail");
            for (String file : files
            ) {
                multipart.addBodyPart(getAttachFilePart(file));
            }

            message.setContent(multipart);
        } catch (Exception e) {
            e.printStackTrace();
            logWriter.logWarning("Attaching files to Mail", e);
        }

        try {
            logWriter.logInfo("SendingMessage");
            Transport.send(message);
        } catch (Exception e) {
            logWriter.logWarning("Cant send message... ", e);
        }
    }

    private BodyPart getMessagePart(String text) throws MessagingException {
        logWriter.logInfo("Set message body: " + text);
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
            logWriter.logInfo("Getting data from file: " + file);
            part.setDataHandler(new DataHandler(new FileDataSource(file)));
        } catch (MessagingException e) {
            logWriter.logWarning("Getting Data Exception", e);
            System.out.println(e.getMessage());
        }
        try {
            logWriter.logInfo("Setting filename: " + file);
            part.setFileName(file);
        } catch (MessagingException e) {
            logWriter.logWarning("Settings File Name Exception", e);
            System.out.println(e.getMessage());
        }
        return part;
    }


}
