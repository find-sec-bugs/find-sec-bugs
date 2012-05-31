package testcode.crypto;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

public class WeakMessageDigest {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest md2Digest = MessageDigest.getInstance("MD2");
        md2Digest.update("123".getBytes());
        printHex(md2Digest.digest());

        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        md5Digest.update("123".getBytes());
        printHex(md5Digest.digest());
    }


    private static void printHex(byte[] bytes) {
        System.out.println(Hex.encodeHexString(bytes));
    }

}
