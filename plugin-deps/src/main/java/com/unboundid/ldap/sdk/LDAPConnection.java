package com.unboundid.ldap.sdk;

public class LDAPConnection implements LDAPInterface {

    public LDAPConnection(String host, int port) {
    }

    public LDAPConnection(String host, int port, String dn, String password) throws LDAPException {
    }

    @Override
    public SearchResult search(String baseDN, SearchScope scope, DereferencePolicy derefPolicy, int sizeLimit, int timeLimit, boolean typesOnly, String filter, String... attributes) {
        return null;
    }

    @Override
    public SearchResult search(String s, SearchScope sub, String filter, String... attributes) {
        return null;
    }

    @Override
    public SearchResultEntry searchForEntry(String baseDN, SearchScope scope, DereferencePolicy derefPolicy, int timeLimit, boolean typesOnly, String filter, String... attributes) {
        return null;
    }

    @Override
    public SearchResultEntry searchForEntry(String baseDN, SearchScope scope, String filter, String... attributes) {
        return null;
    }
}
