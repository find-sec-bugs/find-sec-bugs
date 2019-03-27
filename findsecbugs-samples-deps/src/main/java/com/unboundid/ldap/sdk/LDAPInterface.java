package com.unboundid.ldap.sdk;

public interface LDAPInterface {

    SearchResult search(String baseDN, SearchScope scope, DereferencePolicy derefPolicy, int sizeLimit, int timeLimit, boolean typesOnly, String filter, String... attributes);

    SearchResult search(String s, SearchScope sub, String filter, String... attributes);

    SearchResultEntry searchForEntry(String baseDN, SearchScope scope, DereferencePolicy derefPolicy, int timeLimit, boolean typesOnly, String filter, String... attributes);

    SearchResultEntry searchForEntry(String baseDN, SearchScope scope, String filter, String... attributes);
}
