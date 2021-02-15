package interfaces;

import java.util.Properties;
import java.util.logging.Logger;

public interface Configuratble {
    void logInfo(String msg);
    void logWarning(String msg, Exception e);

}
