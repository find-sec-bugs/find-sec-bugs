package testcode.ldap;

import com.sun.jndi.ldap.LdapCtx;

import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.event.EventDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;
import java.util.Properties;

public class JndiLdapAdditionalSignature {

    public static void moreLdapInjections(String input) throws NamingException {
        //Stub instances
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, "ldap://ldap.example.com");
        props.put(Context.REFERRAL, "ignore");

        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[]{"givenName", "sn"});
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //Various context instance store in various type (class or interface)
        DirContext         context1 = new InitialDirContext(props);
        InitialDirContext  context2 = new InitialDirContext(props);
        InitialLdapContext context3 = new InitialLdapContext();
        LdapContext        context4 = new InitialLdapContext();

        NamingEnumeration<SearchResult> answers;
        answers = context1.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", ctrls);
        answers = context1.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", new Object[0], ctrls);
        answers = context1.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", ctrls);
        answers = context1.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", new Object[0], ctrls);

        answers = context2.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", ctrls);
        answers = context2.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", new Object[0], ctrls);
        answers = context2.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", ctrls);
        answers = context2.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", new Object[0], ctrls);

        answers = context3.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", ctrls);
        answers = context3.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", new Object[0], ctrls);
        answers = context3.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", ctrls);
        answers = context3.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", new Object[0], ctrls);

        answers = context4.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", ctrls);
        answers = context4.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", new Object[0], ctrls);
        answers = context4.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", ctrls);
        answers = context4.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", new Object[0], ctrls);


        //False positive
        answers = context1.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=bob)", ctrls);
        answers = context1.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=bob)", new Object[0], ctrls);
        answers = context1.search("dc=People,dc=example,dc=com", "(uid=bob)", ctrls);
        answers = context1.search("dc=People,dc=example,dc=com", "(uid=bob)", new Object[0], ctrls);
    }

    public void ldapInjectionSunApi(String input) throws NamingException {
        //Stub instances
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, "ldap://ldap.example.com");
        props.put(Context.REFERRAL, "ignore");

        SearchControls ctrls = new SearchControls();
        ctrls.setReturningAttributes(new String[]{"givenName", "sn"});
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        //Two context instances mostly usable with sun specific API
        LdapCtx            context5 = null;
        EventDirContext    context6 = null; //LdapCtx is the only known class to implements to this interface

        NamingEnumeration<SearchResult> answers;
        answers = context5.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", ctrls);
        answers = context5.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", new Object[0], ctrls);
        answers = context5.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", ctrls);
        answers = context5.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", new Object[0], ctrls);

        answers = context6.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", ctrls);
        answers = context6.search(new LdapName("dc=People,dc=example,dc=com"), "(uid=" + input + ")", new Object[0], ctrls);
        answers = context6.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", ctrls);
        answers = context6.search("dc=People,dc=example,dc=com", "(uid=" + input + ")", new Object[0], ctrls);
    }

}
