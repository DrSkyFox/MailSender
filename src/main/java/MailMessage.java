import com.sun.jdi.request.ClassUnloadRequest;
import com.task.MailSendTasks;
import com.task.MailToTask;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

    private void sendEmail(String sendFromEmail, List<String> sendToEmail, String subject, String text, List<String> file) {

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

            if (text != null) {
                message.setText(text);
            } else {
                message.setFileName("Text");
            }

            if (file != null) {
                message.setDataHandler(new DataHandler(new FileDataSource(file)));
                message.setFileName(file);
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
