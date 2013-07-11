package testcode.trustmanager;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class KeyStoresTrustManager implements X509TrustManager {
    private LinkedList<X509TrustManager> trustManagers = new LinkedList<X509TrustManager>();
    private X509Certificate[] acceptedIssuers;

    public KeyStoresTrustManager(KeyStore... keyStores) throws NoSuchAlgorithmException, KeyStoreException {
        super();

        for (KeyStore keystore : keyStores) {
            TrustManagerFactory factory = TrustManagerFactory.getInstance("JKS");
            factory.init(keystore);
            TrustManager[] tms = factory.getTrustManagers();
            if (tms.length == 0) {
                throw new NoSuchAlgorithmException("Unable to load keystore");
            }
            trustManagers.add((X509TrustManager) tms[0]);
        }

        //Build accepted issuers list
        Set<X509Certificate> issuers = new HashSet<X509Certificate>();
        for (X509TrustManager tm : trustManagers) {
            for (X509Certificate issuer : tm.getAcceptedIssuers()) {
                issuers.add(issuer);
            }
        }
        acceptedIssuers = issuers.toArray(new X509Certificate[issuers.size()]);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        CertificateException catchException = null;
        for (X509TrustManager tm : trustManagers) {
            try {
                tm.checkClientTrusted(certificates, authType);
                return;
            } catch (CertificateException e) {
                catchException = e;
            }
        }
        throw catchException;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        CertificateException catchException = null;
        for (X509TrustManager tm : trustManagers) {
            try {
                tm.checkServerTrusted(certificates, authType);
                return;
            } catch (CertificateException e) {
                catchException = e;
            }
        }
        throw catchException;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return acceptedIssuers;
    }
}
