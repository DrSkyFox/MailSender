

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

public class ConfigInit implements Configuratble {

    private Logger logger;
    private final Properties properties;


    public ConfigInit(boolean logOn) throws IOException {
        properties = new Properties();
        if(logOn) {
            initLogger();
        }
        init();
    }

    private void init() throws IOException {

        logInfo("Prepare to read config file");
        Path connectConfig = Paths.get("/configuration/connection_settings.json");
        Path senderConfig = Paths.get("/configuration/sender_settings.json");
        if(!Files.exists(connectConfig)) {
            logInfo("Configuration file 'connection_settings.json' not found.. Create example file config");
            createExampleConfigFile(connectConfig);
        }

        if(!Files.exists(senderConfig)) {
            logInfo("Configuration file 'sender_settings.json' not found.. Create example file config");
            createExampleSendConfig(senderConfig);
        }

    }

    private void readConfigFileJSON() {

    }

    private void initLogger() {
        logger  = Logger.getLogger("MailMessage");

        try {
            FileHandler fileHandler = new FileHandler("log.%u.%g.log", 10000, 5, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println("Cant create log files");
        }
    }

    private void createExampleConfigFile(Path connectConfig) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        logInfo("Write config file: " + connectConfig.toString());
        objectMapper.writeValue(connectConfig.toFile(), new ConnectionSettings("smtp.example.ru",
                "true",
                "465",
                "465",
                "login",
                "password"));
    }

    private void createExampleSendConfig(Path senderConfig) throws IOException {
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

        logInfo("Write config file: " + senderConfig.toString());
        objectMapper.writeValue(senderConfig.toFile(), new MailSendTasks(mailToTaskList));
    }

    @Override
    public void logInfo(String msg) {
        if(logger != null) {
            logger.info(String.format("Class: %s, message: %s", this.getClass().getName(), msg));
        }
    }

    @Override
    public void logWarning(String msg, Exception e) {
        if(logger != null) {
            logger.warning(String.format("Class: %s, message: %s , exception: %s", this.getClass().getName(), msg, e.getMessage()));
        }
    }


    @Override
    public Properties getProperties() {
        return properties;
    }
}
