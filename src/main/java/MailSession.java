import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Objects;
import java.util.Properties;

public class MailSession {

    private Session session;
    private Properties properties;
    private String user;
    private String password;

    public MailSession(Properties properties, String user, String password) {
        this.properties = properties;
        this.user = user;
        this.password = password;
    }

    public Session getSession() {
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
