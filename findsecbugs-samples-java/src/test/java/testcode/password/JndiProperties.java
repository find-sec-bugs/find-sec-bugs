package testcode.password;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
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

    public void bug3(String inputNom) throws NamingException {
        DirContext directoryContext = null;
        Hashtable<String, String> env = new Hashtable<String, String>();

        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://ldapserver.hex.org:389");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=Manager");
        env.put(Context.SECURITY_CREDENTIALS, "ld4pp455w0rd");

        directoryContext = new InitialDirContext(env);

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
