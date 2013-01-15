package com.unboundid.ldap.sdk;

import java.util.List;

public class SearchResult {
    private int entryCount;

    public int getEntryCount() {
        return entryCount;
    }

    public List<SearchResultEntry> getSearchEntries() {
        return null;
    }
}
