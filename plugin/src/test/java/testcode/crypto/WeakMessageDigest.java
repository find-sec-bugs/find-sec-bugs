package testcode.crypto;

import testcode.util.HexUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        System.out.println(HexUtil.toString(bytes));
    }

}
