package testcode.crypto;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;

public class UnencryptedServerSocket {

    static void sslServerSocket() throws IOException {
        ServerSocket ssoc = SSLServerSocketFactory.getDefault().createServerSocket(1234);
        ssoc.close();
    }

    static void plainServerSocket() throws IOException {
        ServerSocket ssoc = new ServerSocket(1234);
        ssoc.close();
    }

    static void otherConstructors() throws IOException {
        ServerSocket ssoc1 = new ServerSocket();
        ssoc1.close();
        ServerSocket ssoc2 = new ServerSocket(1234, 10);
        ssoc2.close();
        byte[] address = {127, 0, 0, 1};
        ServerSocket ssoc3 = new ServerSocket(1234, 10, InetAddress.getByAddress(address));
        ssoc3.close();
    }

}
