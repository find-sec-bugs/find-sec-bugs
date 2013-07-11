package testcode.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BadHexa {
    public static void main(String[] args) throws Exception {
        String good = goodHash("12345");
        String bad = badHash("12345");

        System.out.println(String.format("%s (len=%d) != %s (len=%d)", good, good.length(), bad, bad.length()));

        //For the hash ABC the 5th byte become weaker because of the trailing 0 being trim (06 -> 6)
        //Actual      : ..0679.. => ..679..
        //Collision 1 : ..6709.. => ..679..
        //Actual      : ..7D06.. => ..7D6..
        //Collision 2 : ..07D6.. => ..7D6..
    }

    public static String goodHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] resultBytes = md.digest(password.getBytes("UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : resultBytes) {
            stringBuilder.append(String.format("%02X", b));
        }

        return stringBuilder.toString();
    }

    public static String badHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] resultBytes = md.digest(password.getBytes("UTF-8"));

        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : resultBytes) {
            stringBuilder.append(Integer.toHexString(b & 0xFF));
        }

        return stringBuilder.toString();
    }
}
