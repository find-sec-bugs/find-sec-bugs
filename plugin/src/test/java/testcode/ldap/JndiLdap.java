package testcode.ldap;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Properties;

/**
 * Taken from : http://stackoverflow.com/a/4412867/89769
 */
public class JndiLdap {

    static boolean authenticate(String username, String password) {
        try {
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            props.put(Context.PROVIDER_URL, "ldap://ldap.example.com");
            props.put(Context.REFERRAL, "ignore");
            props.put(Context.SECURITY_PRINCIPAL, dnFromUser(username));
            props.put(Context.SECURITY_CREDENTIALS, password);

            new InitialDirContext(props);
            return true;
        } catch (NamingException e) {
            return false;
        }

    }

    private static String dnFromUser(String username) throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, "ldap://ldap.example.com");
        props.put(Context.REFERRAL, "ignore");

        InitialDirContext context = new InitialDirContext(props);

        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[]{"givenName", "sn"});
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration<SearchResult> answers = context.search("dc=People,dc=example,dc=com", "(uid=" + username + ")", ctrls);
        SearchResult result = answers.next();

        return result.getNameInNamespace();
    }
}
