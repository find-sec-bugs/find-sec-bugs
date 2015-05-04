package testcode.crypto;

import javax.crypto.Cipher;

public class CipherNoIntegrity {

    public static void main(String[] args) throws Exception {
        Cipher.getInstance("AES/GCM/..."); // ok
        Cipher.getInstance("AES"); // ECB and no integrity
        Cipher.getInstance("AES/CTR/NoPadding"); // no integrity
        Cipher.getInstance("AES/ECB/PKCS5Padding"); // ECB and no integrity
        Cipher.getInstance("AES/CBC/PKCS5Padding"); // oracle and no integrity
        Cipher.getInstance("RSA"); // ok
        Cipher.getInstance("RSA/ECB/PKCS1Padding"); // ok
        Cipher.getInstance(args[0]); // ok
    }
}
