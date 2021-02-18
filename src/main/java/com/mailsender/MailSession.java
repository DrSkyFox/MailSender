package com.mailsender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Objects;
import java.util.Properties;

public class MailSession {

    private static final Logger logger = LogManager.getLogger(MailSession.class.getName());

    private Session session;
    private Properties properties;
    private String user;
    private String password;


    public MailSession(Properties properties, String user, String password) {
        logger.info(String.format("Entering parameters: \n user = %s \n password = %s \n properties = { %s }", user,password, properties.toString()));
        this.properties = properties;
        this.user = user;
        this.password = password;
    }

    public Session getSession() {
        logger.info("Getting session");
        if(Objects.isNull(session)) {
            session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, password);
                        }
                    });
            return session;
        }
        return session;
    }

}
