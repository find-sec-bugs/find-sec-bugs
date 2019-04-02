package testcode.ldap;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchScope;

/**
 * Taken from : http://stackoverflow.com/a/4412867/89769
 */
public class UnboundIdLdap {

    static boolean authenticate(String username, String password) throws LDAPException {
        LDAPConnection ldap = new LDAPConnection("ldap.example.com", 389);
        SearchResult sr = ldap.search("dc=People,dc=example,dc=com", SearchScope.SUB, "(uid=" + username + ")");
        if (sr.getEntryCount() == 0)
            return false;

        String dn = sr.getSearchEntries().get(0).getDN();

        try {
            ldap = new LDAPConnection("ldap.example.com", 389, dn, password);
            return true;
        } catch (LDAPException e) {
            if (e.getResultCode() == ResultCode.INVALID_CREDENTIALS)
                return false;

            throw e;
        }
    }
}
