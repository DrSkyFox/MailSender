

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.MailSendTasks;
import com.task.MailToTask;
import interfaces.Configuratble;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ConfigInit  {

    private LogWriter logWriter;
    private Path connectConfig;
    private Path senderConfig;
    private Boolean fileExists;


    public ConfigInit(LogWriter logWriter) throws IOException {
        this.logWriter = logWriter;
        init();
    }

    private void init() throws IOException {

        logWriter.logInfo("Prepare to read config file");
        connectConfig = Paths.get("/configuration/connection_settings.json");
        senderConfig = Paths.get("/configuration/sender_settings.json");
        if(!Files.exists(connectConfig)) {
            logWriter.logInfo("Configuration file 'connection_settings.json' not found.. Create example file config");
            createExampleConfigFile();
            fileExists = false;
        }
        if(!Files.exists(senderConfig)) {
            logWriter.logInfo("Configuration file 'sender_settings.json' not found.. Create example file config");
            createExampleSendConfig();
            fileExists = false;
        }
        fileExists = true;
    }

    public Boolean getFileExists() {
        return fileExists;
    }

    public MailSession readConfigFileAndGetSession() {
        logWriter.logInfo("Start read file: 'connection_settings.json'... ");
        ObjectMapper mapper = new ObjectMapper();
        Properties properties = new Properties();
        try {
            ConnectionSettings settings =  mapper.readValue(connectConfig.toFile(), ConnectionSettings.class);

            logWriter.logInfo("readConfigFileAndGetSession: Complete read file: 'connection_settings.json'... ");
            logWriter.logInfo("readConfigFileAndGetSession: settings info: " + settings.toString());
            properties.put("mail.smtp.host", settings.getSmtpHost());
            properties.put("mail.smtp.auth", settings.getAuth());
            properties.put("mail.smtp.port", settings.getPort());
            properties.put("mail.smtp.socketFactory.port", settings.getSocketFactory());
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            logWriter.logInfo("readConfigFileAndGetSession: Getting MailSession");
            return new MailSession(properties, settings.getAccountFullName(), settings.getPassword());
        } catch (IOException e) {
            logWriter.logWarning("readConfigFileAndGetSession: Exception", e);
        }
        return null;
    }

    public MailSendTasks readConfigFileAndGetTask() {
        logWriter.logInfo("readConfigFileAndGetTask: Start read file 'sender_settings.json'...");
        ObjectMapper mapper = new ObjectMapper();

        try {
            MailSendTasks mailSendTasks = mapper.readValue(senderConfig.toFile(), MailSendTasks.class);
            logWriter.logInfo("readConfigFileAndGetTask: Complete read file: 'connection_settings.json'... ");
            logWriter.logInfo("readConfigFileAndGetTask: settings info: " + mailSendTasks.toString());
            return mailSendTasks;
        } catch (IOException e) {
            logWriter.logWarning("readConfigFileAndGetTask: Exception", e);
        }
        return null;
    }


    private void createExampleConfigFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        logWriter.logInfo("Write config file: " + connectConfig.toString());
        objectMapper.writeValue(connectConfig.toFile(), new ConnectionSettings("smtp.example.ru",
                "true",
                "465",
                "465",
                "login",
                "password"));
    }

    private void createExampleSendConfig() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
//            (String fromEmail, List<String> sendListToEmail, List<String> attachedFilesInDirectory, String subject, String text)

        List<String> stringList = new ArrayList<>();
        stringList.add("sendToEmail1@targetmail.com");
        stringList.add("sendToEmail2@targetmail.com");
        List<String> stringList1 = new ArrayList<>();
        stringList1.add("test");
        stringList1.add("d:\\test");
        List<MailToTask> mailToTaskList = new ArrayList<>();
        mailToTaskList.add(new MailToTask(
                "emailFrom@mymail.com",
                stringList,
                stringList1,
                "Subject Of Email",
                "SomeText", true));
        mailToTaskList.add(new MailToTask(
                "emailFrom@mymail.com",
                stringList,
                stringList1,
                "Subject Of Email",
                "SomeText",false));

        logWriter.logInfo("Write config file: " + senderConfig.toString());
        objectMapper.writeValue(senderConfig.toFile(), new MailSendTasks(mailToTaskList));
    }




}
