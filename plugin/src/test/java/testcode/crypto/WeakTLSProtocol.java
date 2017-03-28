package testcode.crypto;

import java.security.NoSuchAlgorithmException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;

import javax.net.ssl.SSLContext;

public class WeakTLSProtocol {

    public static void main(String[] args) {
        HttpClient client1 = new DefaultHttpClient(); // BAD

        HttpClient client2 = new SystemDefaultHttpClient(); // OK
        
        try {
          SSLContext context1 = SSLContext.getInstance("SSL"); // BAD
          
          SSLContext context2 = SSLContext.getInstance("TLS"); // OK
        } catch (NoSuchAlgorithmException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }

}
