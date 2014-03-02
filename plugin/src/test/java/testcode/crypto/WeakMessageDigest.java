package testcode.crypto;

import org.apache.commons.codec.digest.DigestUtils;
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

        MessageDigest sha1Digest = MessageDigest.getInstance("SHA1");
        sha1Digest.update("123".getBytes());
        printHex(sha1Digest.digest());

        apacheApiVariations();
    }

    public static void apacheApiVariations() {

        printHex(DigestUtils.getMd5Digest().digest("123".getBytes()));
        printHex(DigestUtils.getMd2Digest().digest("123".getBytes()));
        printHex(DigestUtils.getSha1Digest().digest("123".getBytes()));
        printHex(DigestUtils.getShaDigest().digest("123".getBytes()));

        printHex(DigestUtils.getDigest("md2").digest("123".getBytes()));
        printHex(DigestUtils.getDigest("md5").digest("123".getBytes()));
        printHex(DigestUtils.getDigest("sha1").digest("123".getBytes()));

        System.out.println(DigestUtils.md2Hex("123".getBytes()));
        System.out.println(DigestUtils.md5Hex("123".getBytes()));
        System.out.println(DigestUtils.sha1Hex("123".getBytes()));
    }


    private static void printHex(byte[] bytes) {
        System.out.println(HexUtil.toString(bytes));
    }

}
