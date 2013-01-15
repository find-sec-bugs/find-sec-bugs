package com.unboundid.ldap.sdk;

public class SearchScope {
    public static final int BASE_INT_VALUE = 0;
    public static final SearchScope BASE = new SearchScope(BASE_INT_VALUE);
    public static final int ONE_INT_VALUE = 1;
    public static final SearchScope ONE = new SearchScope(ONE_INT_VALUE);
    public static final int SUB_INT_VALUE = 2;
    public static final SearchScope SUB = new SearchScope(SUB_INT_VALUE);
    public static final int SUBORDINATE_SUBTREE_INT_VALUE = 3;
    public static final SearchScope SUBORDINATE_SUBTREE = new SearchScope(SUBORDINATE_SUBTREE_INT_VALUE);

    private final int intValue;

    private SearchScope(int intValue) {
        this.intValue = intValue;
    }
}
