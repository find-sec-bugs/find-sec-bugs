package org.apache.commons.mail;

public abstract class Email {

    protected String hostName;

    private boolean sslOnConnect;

    private boolean sslCheckServerIdentity;


    public void setHostName(final String aHostName) {
        this.hostName = aHostName;
    }

    public Email setSSLOnConnect(final boolean ssl) {
        this.sslOnConnect = ssl;
        return this;
    }

    public Email setSSLCheckServerIdentity(final boolean sslCheckServerIdentity) {
        this.sslCheckServerIdentity = sslCheckServerIdentity;
        return this;
    }
}
