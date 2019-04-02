package testcode.logging;

import org.slf4j.Logger;

public class Slf4jSample {

    public void slf4j(Logger log,DataClass tainted,String tainted2) {
        String safe = "";
        //Unsafe
        log.info(tainted.input);
        log.info(tainted.input,safe);
        log.info(safe,tainted.input);
        log.info(safe,new Object[] {tainted.input});
        log.info(safe,safe,tainted.input);
        log.info(tainted2);
        log.info(tainted2,safe);
        log.info(safe,tainted2);
        log.info(safe,new Object[] {tainted2});
        log.info(safe,safe,tainted2);

        // safe

        log.info(safe,new Object[] {""});
    }

    class DataClass {
        String input;
    }
}
