package com.mailsender;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailsender.settings.ConnectionSettings;
import com.mailsender.settings.MailToTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class ConfigInit  {

    private static final Logger logger = LogManager.getLogger(ConfigInit.class.getName());


    private Path connectionConfig;
    private Path taskConfig;
    private Boolean fileExists;


    public ConfigInit(Path connectionConfig, Path taskConfig) {
        this.connectionConfig = connectionConfig;
        this.taskConfig = taskConfig;
        init();
    }

    private void init()  {
        fileExists = true;
        logger.info("Prepare to read config file: connection_settings.json, sender_settings.json");

        connectionConfig = Paths.get("connection_settings.json");
        taskConfig = Paths.get("sender_settings.json");


        if(!Files.exists(connectionConfig)) {
            logger.info("Configuration file 'connection_settings.json' not found.. Create example file config");
            createExampleConfigFile();
            fileExists = false;
        }
        if(!Files.exists(taskConfig)) {
            logger.info("Configuration file 'sender_settings.json' not found.. Create example file config");
            createExampleSendConfig();
            fileExists = false;
        }
        if(!fileExists) {
            return;
        }

    }


    public MailSession readConfigFileAndGetSession() {
        logger.info("Getting Session's parameters");
        logger.info("Start read configuration file: {}", connectionConfig.toString());
        ObjectMapper mapper = new ObjectMapper();
        Properties properties = new Properties();
        try {
            ConnectionSettings settings =  mapper.readValue(connectionConfig.toFile(), ConnectionSettings.class);

            logger.info("Read file {} successful. Configuration is {}", connectionConfig.toString(), settings.toString());

            properties.put("mail.smtp.host", settings.getSmtpHost());
            properties.put("mail.smtp.auth", settings.getAuth());
            properties.put("mail.smtp.port", settings.getPort());
            properties.put("mail.smtp.socketFactory.port", settings.getSocketFactory());
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            return new MailSession(properties, settings.getAccountFullName(), settings.getPassword());
        } catch (IOException e) {
            logger.error("Error in reading configuration file", e);
        }
        return null;
    }

    public List<MailToTask> readConfigFileAndGetTask() {
        logger.info("Getting task's parameters");
        logger.info("Start read configuration file: {}", taskConfig.toString());
        ObjectMapper mapper = new ObjectMapper();

        try {
            List<MailToTask> mailSendTasks = mapper.readValue(taskConfig.toFile(), new TypeReference<List<MailToTask>>() {});
            logger.info("Read file {} successful. Configuration is {}", taskConfig.toString(), mailSendTasks.toString());
            return mailSendTasks;
        } catch (IOException e) {
            logger.error("Error in reading configuration file ", e);
        }
        return null;
    }


    private void createExampleConfigFile(){
        logger.info("Creating example configuration file: connection_settings.json");
        ObjectMapper objectMapper = new ObjectMapper();

        ConnectionSettings settings = new ConnectionSettings(
                "smtp.example.ru",
                "true",
                "465",
                "465",
                "login",
                "password");
        logger.info("Writing data: {} to file: {}", settings, connectionConfig.toString());

        try {
            objectMapper.writeValue(connectionConfig.toFile(), settings);
        } catch (IOException e) {
            logger.error("Cant write data to file", e);
        }
    }

    private void createExampleSendConfig() {
        logger.info(String.format("Creating example configuration file: %s", taskConfig.toString()));
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> stringList = new ArrayList<>();
        stringList.add("sendToEmail1@targetmail.com");
        stringList.add("sendToEmail2@targetmail.com");

        List<String> stringList1 = new ArrayList<>();
        stringList1.add("d:\\test2");
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

        logger.info("Writing data: {} to file {}}", mailToTaskList.toString(), taskConfig.toString());
        try {
            objectMapper.writeValue(taskConfig.toFile(), mailToTaskList);
        } catch (IOException e) {
            logger.error("Cant write data to file", e);
        }
    }

}
