import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) {


        LogWriter logWriter  = new LogWriter();
        logWriter.logInfo("Start program..............................................");
        Iterator<String> params = Arrays.stream(args).iterator();
        ConfigInit configInit;
        try {
            configInit = new ConfigInit(logWriter);
        } catch (IOException e) {
            logWriter.logWarning("Cant init configuration", e);
            return;
        }
        logWriter.logInfo("Configuration complete");
        MailMessage message = new MailMessage(configInit, logWriter);
        logWriter.logInfo("Start Do Tasks");
        message.doAllTask();


    }
}
