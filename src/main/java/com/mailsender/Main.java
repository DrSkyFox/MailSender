package com.mailsender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;



public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {



        Path connectionConfig = Path.of("SettingOfConnection.json");
        Path taskConfig = Path.of("TaskSettings.json");

        if(checkSyntaxArguments(args) && args.length > 0) {
            help();
            return;
        } else {
            Iterator<String> stringIterator = Arrays.stream(args).iterator();
            while (stringIterator.hasNext()) {
                if(stringIterator.next().equals("-connectConf")) {
                    connectionConfig = Path.of(stringIterator.next());
                } else if(stringIterator.next().equals("-connectConf")) {
                    taskConfig = Path.of(stringIterator.next());
                }else if(stringIterator.next().equals("-help")) {
                    if (stringIterator.next().equals("connectConf")) {
                        helpConnectConf();
                    } else if(stringIterator.next().equals("taskConf")) {
                        helpTaskConf();
                    }
                }
            }
        }



        ConfigInit configInit = new ConfigInit(connectionConfig,taskConfig);

        MailMessage message = new MailMessage(configInit);
        message.doAllTask();
    }



    private static boolean checkSyntaxArguments(String [] args) {
        if(args.length%2 != 0) {
            return false;
        }
        for (int i = 0; i < args.length; i=i+2) {
            if(!args[i].startsWith("-")) {
                return false;
            }
        }
        return true;
    }



    private static void helpConnectConf() {
        System.out.println(String.format(
                "Arguments support: \n " +
                        "Arguments: -connectConf @filename@ \n " +
                        "Configuration file storing data about smtp server settings. \n " +
                        "As well as data about the account from which the letters will be sent. \n" +
                        "Examples on Windows OS: 1. -connectConf setting.json 2. -connectConf d:\somedir\settings.json"));
    }
    private static void helpTaskConf() {
        System.out.println(String.format(
                "Arguments: -taskConf @filename@ \n " +
                        "A configuration file that describes data on sending tasks \n." +
                        "Examples on Windows OS: 1. -taskConf tasks.json 2. -taskConf d:\somedir\tasks.json"));
    }
    private static void help() {
        System.out.println(String.format(
                        "Arguments: -help \n" +
                                "The command shows help information for the arguments being entered"));
        System.out.println(String.format(
                "Syntax arguments.. Example C:>MailSender-#.# -taskConf @filename@ -connectConf @filename@"));
    }

}
