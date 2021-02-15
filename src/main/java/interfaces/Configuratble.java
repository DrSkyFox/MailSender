package interfaces;

import java.util.Properties;
import java.util.logging.Logger;

public interface Configuratble {
    Properties getProperties();
    void logInfo(String msg);
    void logWarning(String msg, Exception e);

}
