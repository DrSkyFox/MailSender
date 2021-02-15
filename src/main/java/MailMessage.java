import com.task.MailSendTasks;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

public class MailMessage {

    private MailSession session;
    private MailSendTasks tasks;
    public MailMessage(boolean logOn) {
        ConfigInit configInit = new ConfigInit(logOn);
    }


    public void sendEmail(String sendFromEmail, String subject, String text, String file) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(sendFromEmail));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("abonement.support@shelter.ru"));

            if(sendFromEmail != null) {
                message.setSubject(subject);
            } else {
                message.setSubject("Subject");
            }

            if (text != null) {
                message.setText(text);
            } else {
                message.setFileName("Text");
            }

            if(file != null) {
                message.setDataHandler(new DataHandler(new FileDataSource(file)));
                message.setFileName(file);
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
