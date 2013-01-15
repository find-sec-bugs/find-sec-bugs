package testcode.password;

import javax.naming.Context;
import java.util.Properties;

public class JndiProperites {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(Context.SECURITY_CREDENTIALS, "oups");
    }
}
