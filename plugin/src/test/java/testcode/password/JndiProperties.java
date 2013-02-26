package testcode.password;

import javax.naming.Context;
import java.util.Properties;

public class JndiProperties {

    public void bug1() {
        Properties props = new Properties();
        props.put(Context.SECURITY_CREDENTIALS, "oups");
    }

    public void bug2() {
        Properties props = new Properties();
        props.put("java.naming.security.credentials", "oups");
    }

    public void ok1(String input) {
        Properties props = new Properties();
        props.put(Context.SECURITY_CREDENTIALS, input);
    }

    public void ok2() {
        Properties props = new Properties();
        props.put(Context.LANGUAGE, "fr-CA"); //not sensitive information
    }
}
