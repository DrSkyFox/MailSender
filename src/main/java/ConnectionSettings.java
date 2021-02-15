public class ConnectionSettings {

    private String smtpHost;
    private String auth;
    private String port;
    private String socketFactory;
    private String accountFullName;
    private String password;

    public ConnectionSettings(String smtpHost, String auth, String port, String socketFactory) {
        this.smtpHost = smtpHost;
        this.auth = auth;
        this.port = port;
        this.socketFactory = socketFactory;
    }

    public ConnectionSettings(String smtpHost, String auth, String port, String socketFactory, String accountFullName, String password) {
        this.smtpHost = smtpHost;
        this.auth = auth;
        this.port = port;
        this.socketFactory = socketFactory;
        this.accountFullName = accountFullName;
        this.password = password;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSocketFactory() {
        return socketFactory;
    }

    public void setSocketFactory(String socketFactory) {
        this.socketFactory = socketFactory;
    }

    public String getAccountFullName() {
        return accountFullName;
    }

    public void setAccountFullName(String accountFullName) {
        this.accountFullName = accountFullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
