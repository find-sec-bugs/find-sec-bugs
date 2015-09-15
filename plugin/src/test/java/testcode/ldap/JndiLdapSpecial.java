package testcode.ldap;

import javax.naming.NamingException;

public class JndiLdapSpecial {

    //Taken from BenchmarkTest00012.java (https://github.com/OWASP/Benchmark/blob/master/src/main/java/org/owasp/benchmark/testcode/BenchmarkTest00012.java)
    public static void main(String param) throws NamingException {
        javax.naming.directory.DirContext ctx = null;
        String base = "ou=users,ou=system";
        javax.naming.directory.SearchControls sc = new javax.naming.directory.SearchControls();
        sc.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE);
        String filter = "(&(objectclass=person))(|(uid="+param+")(street={0}))";
        Object[] filters = new Object[]{"The streetz 4 Ms bar"};
        System.out.println("Filter " + filter);
        javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> results = ctx.search(base, filter,filters, sc);
    }

}
