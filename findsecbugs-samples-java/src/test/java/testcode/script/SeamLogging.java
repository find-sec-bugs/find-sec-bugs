package testcode.script;

import org.jboss.seam.log.Log;

public class SeamLogging {

    public void variousLogMessageUnsafe(String input,Log log) {
        log.debug(input);
        log.debug(input,"1","2");
        log.debug(input,new RuntimeException("TEST"));
        log.debug(input,new RuntimeException("TEST"),"1","2");
        log.error(input);
        log.error(input, "1", "2");
        log.error(input, new RuntimeException("TEST"));
        log.error(input, new RuntimeException("TEST"), "1", "2");
        log.fatal(input);
        log.fatal(input, "1", "2");
        log.fatal(input, new RuntimeException("TEST"));
        log.fatal(input, new RuntimeException("TEST"), "1", "2");
        log.info(input);
        log.info(input, "1", "2");
        log.info(input, new RuntimeException("TEST"));
        log.info(input, new RuntimeException("TEST"), "1", "2");
        log.trace(input);
        log.trace(input, "1", "2");
        log.trace(input, new RuntimeException("TEST"));
        log.trace(input, new RuntimeException("TEST"), "1", "2");
        log.warn(input);
        log.warn(input, "1", "2");
        log.warn(input, new RuntimeException("TEST"));
        log.warn(input, new RuntimeException("TEST"), "1", "2");
    }

    public void variousLogMessageSafe(Log log) {
        String input = "";
        log.debug(input);
        log.debug(input,"1","2");
        log.debug(input, new RuntimeException("TEST"));
        log.debug(input, new RuntimeException("TEST"), "1", "2");
        log.error(input);
        log.error(input, "1", "2");
        log.error(input, new RuntimeException("TEST"));
        log.error(input, new RuntimeException("TEST"), "1", "2");
        log.fatal(input);
        log.fatal(input, "1", "2");
        log.fatal(input, new RuntimeException("TEST"));
        log.fatal(input, new RuntimeException("TEST"), "1", "2");
        log.info(input);
        log.info(input, "1", "2");
        log.info(input, new RuntimeException("TEST"));
        log.info(input, new RuntimeException("TEST"), "1", "2");
        log.trace(input);
        log.trace(input, "1", "2");
        log.trace(input, new RuntimeException("TEST"));
        log.trace(input, new RuntimeException("TEST"), "1", "2");
        log.warn(input);
        log.warn(input, "1", "2");
        log.warn(input, new RuntimeException("TEST"));
        log.warn(input, new RuntimeException("TEST"), "1", "2");
    }
}
