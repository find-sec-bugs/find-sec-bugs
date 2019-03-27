package testcode.ldap;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;


public class AnonymousLdap {
	private final static String ldapURI = "ldaps://ldap.server.com/dc=ldap,dc=server,dc=com";
	private final static String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";

	private static DirContext ldapContext (Hashtable <String,String>env) throws Exception {
		env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		env.put(Context.PROVIDER_URL, ldapURI);
        env.put(Context.SECURITY_AUTHENTICATION, "none");
		DirContext ctx = new InitialDirContext(env);
		return ctx;
	}

	public static boolean testBind (String dn, String password) throws Exception {
		Hashtable<String,String> env = new Hashtable <String,String>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple"); //false positive
		env.put(Context.SECURITY_PRINCIPAL, dn);
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			ldapContext(env);
		}
		catch (javax.naming.AuthenticationException e) {
			return false;
		}
		return true;
	}
}