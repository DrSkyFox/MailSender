import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogWriter {

    private static final  Logger logger = Logger.getLogger("MailMessage");

    public LogWriter() {
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

    public void logInfo(String msg) {
        if(logger != null) {
            logger.info(String.format("Class: %s, message: %s", this.getClass().getName(), msg));
        }
    }

    public void logWarning(String msg, Exception e) {
        if(logger != null) {
            logger.warning(String.format("Class: %s, message: %s , exception: %s", this.getClass().getName(), msg, e.getMessage()));
        }
    }


}
