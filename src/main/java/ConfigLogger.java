import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ConfigLogger {

    private static final  Logger logger = Logger.getLogger("MailMessage");

    public ConfigLogger() {
        initLogger();
    }

    private void initLogger() {

        try {
            FileHandler fileHandler = new FileHandler("log.%u.%g.log", 10000, 5, true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.out.println("Cant create log files");
        }
    }



}
