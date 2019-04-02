package testcode;

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.*;

public class Logging {
    public static HttpServletRequest req;
    @SuppressWarnings( "deprecation" )
    public void javaUtilLogging() {
        String tainted = req.getParameter("test");
        String safe = "safe";
        Logger logger = Logger.getLogger(Logging.class.getName());
        logger.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        logger.config(tainted);
        logger.entering(tainted, safe);
        logger.entering("safe", safe, tainted);
        logger.entering(safe, "safe", new String[]{tainted});
        logger.exiting(safe, tainted);
        logger.exiting(safe, "safe", tainted);
        logger.fine(tainted);
        logger.finer(tainted.trim());
        logger.finest(tainted);
        logger.info(tainted);
        logger.log(Level.INFO, tainted);
        logger.log(Level.INFO, tainted, safe);
        logger.log(Level.INFO, "safe", new String[]{tainted});
        logger.log(Level.INFO, tainted, new Exception());
        logger.logp(Level.INFO, tainted, safe, "safe");
        logger.logp(Level.INFO, safe, "safe", tainted, safe);
        logger.logp(Level.INFO, "safe", safe.toLowerCase(), safe, new String[]{tainted});
        logger.logp(Level.INFO, tainted, safe, safe, new Exception());
        logger.logp(Level.INFO, tainted, "safe", (Supplier<String>) null);
        logger.logp(Level.INFO, "safe", tainted, new Exception(), (Supplier<String>) null);
        logger.logrb(Level.INFO, safe, safe, (ResourceBundle) null, "safe", tainted);
        logger.logrb(Level.INFO, tainted, safe, (ResourceBundle) null, safe, new Exception());
        logger.logrb(Level.INFO, tainted, safe, "bundle", safe);
        logger.logrb(Level.INFO, safe, tainted, "bundle", safe, safe);
        logger.logrb(Level.INFO, tainted, "safe", "bundle", safe, new String[]{safe});
        logger.logrb(Level.INFO, safe, safe, "bundle", tainted, new Exception());
        logger.severe(tainted + "safe" + safe);
        logger.throwing("safe", tainted.replace('\r', ' '), new Exception()); // still insecure (LF not replaced)
        logger.warning(tainted.replaceAll("\n", "")); // still insecure (CR not replaced)

        // these should not be reported
        logger.fine(safe);
        logger.log(Level.INFO, "safe".toUpperCase(), safe + safe);
        logger.logp(Level.INFO, safe, safe, safe, new String[]{safe});
        logger.logrb(Level.INFO, safe, safe, tainted + "bundle", safe); // bundle name can be tainted
        logger.throwing(safe, safe, new Exception());
        logger.info(tainted.replace('\n', ' ').replace('\r', ' '));
        String encoded = tainted.replace("\r", "").toUpperCase();
        encoded = "safe" + encoded.toLowerCase();
        logger.warning(encoded.replace("\n", " (new line)"));
        logger.fine(tainted.replaceAll("[\r\n]+", ""));
    }

}
