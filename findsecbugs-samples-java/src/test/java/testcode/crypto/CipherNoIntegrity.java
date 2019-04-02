package testcode.crypto;

import javax.crypto.Cipher;

public class CipherNoIntegrity {

    public static void main(String[] args) throws Exception {
        Cipher.getInstance("AES/GCM/..."); // ok
        Cipher.getInstance("AES"); // ECB and no integrity
        Cipher.getInstance("DES/CTR/NoPadding", "BC"); // no integrity
        Cipher.getInstance("DESede/ECB/PKCS5Padding"); // ECB and no integrity
        Cipher.getInstance("AES/CBC/PKCS5Padding"); // oracle and no integrity
        Cipher.getInstance("RSA"); // ok
        Cipher.getInstance("RSA/ECB/PKCS1Padding"); // ok
        Cipher.getInstance(args[0]); // ok
        Cipher.getInstance("ECIES"); // ok this is elliptic curve
    }
    
    private Cipher cipher;
    public void x() throws Exception {
        cipher = Cipher.getInstance("AES/CTR/NoPadding");
    }
}
