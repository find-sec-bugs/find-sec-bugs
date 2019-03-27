package com.unboundid.ldap.sdk;

public class ResultCode implements java.io.Serializable {

    public static final int INVALID_CREDENTIALS_INT_VALUE = 49;
    public static final ResultCode INVALID_CREDENTIALS = new ResultCode(INVALID_CREDENTIALS_INT_VALUE);

    private final int intValue;

    private ResultCode(int intValue) {
        this.intValue = intValue;
    }


}
