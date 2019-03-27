package testcode.crypto.ssldisabler;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class SslDisablerUsage {
    
    public void useAllHosts() {
        HttpsURLConnection.setDefaultHostnameVerifier(new AllHosts());
    }

    public void useTrustAllManager() throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = new TrustManager[] { new TrustAllManager() };
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    public void useSecurityBypasser() {
        SecurityBypasser.destroyAllSSLSecurityForTheEntireVMForever();
    }
}
