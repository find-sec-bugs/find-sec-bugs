package testcode.crypto;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class UnencryptedSocket {

    public static void main(String[] args) throws IOException {
        System.out.println("== ssl socket ==");
        sslSocket();
        System.out.println("== plain socket ==");
        plainSocket();
    }

    static void sslSocket() throws IOException {
        Socket soc = SSLSocketFactory.getDefault().createSocket("www.google.com", 443);
        doGetRequest(soc);
    }

    static void plainSocket() throws IOException {
        Socket soc = new Socket("www.google.com", 80);
        doGetRequest(soc);
    }

    static void otherConstructors() throws IOException {
        Socket soc1 = new Socket("www.google.com", 80, true);
        doGetRequest(soc1);
        byte[] address = {127, 0, 0, 1};
        Socket soc2 = new Socket("www.google.com", 80, InetAddress.getByAddress(address), 13337);
        doGetRequest(soc2);
        byte[] remoteAddress = {74, 125, (byte) 226, (byte) 193};
        Socket soc3 = new Socket(InetAddress.getByAddress(remoteAddress), 80);
        doGetRequest(soc2);
    }

    static void doGetRequest(Socket soc) throws IOException {
        PrintWriter w = new PrintWriter(soc.getOutputStream());
        w.write("GET / HTTP/1.0\nHost: www.google.com\n\n");
        w.flush();

        BufferedReader r = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        String line = null;
        while ((line = r.readLine()) != null) {
            System.out.println(line);
        }
        soc.close();
    }
}
