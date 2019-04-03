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

        MessageDigest sha1Digest = MessageDigest.getInstance("SHA");
        sha1Digest.update("123".getBytes());
        printHex(sha1Digest.digest());
        
        MessageDigest sha1Digest2 = MessageDigest.getInstance("SHA1");
        sha1Digest2.update("123".getBytes());
        printHex(sha1Digest2.digest());

        MessageDigest sha1Digest3 = MessageDigest.getInstance("SHA-1");
        sha1Digest3.update("123".getBytes());
        printHex(sha1Digest3.digest());

        MessageDigest sha256Digest = MessageDigest.getInstance("SHA256"); //OK!
        sha256Digest.update("123".getBytes());
        printHex(sha256Digest.digest());

        MessageDigest sha512Digest = MessageDigest.getInstance(getDigest()); //This parameter is unknown
        sha512Digest.update("123".getBytes());
        printHex(sha512Digest.digest());

        apacheApiVariations();
    }

    @SuppressWarnings("deprecation")
    public static void apacheApiVariations() {
        printHex(DigestUtils.getMd2Digest().digest("123".getBytes()));
        printHex(DigestUtils.getMd5Digest().digest("123".getBytes()));
        printHex(DigestUtils.getDigest("md2").digest("123".getBytes()));
        printHex(DigestUtils.getDigest("md5").digest("123".getBytes()));
        DigestUtils.md2("123".getBytes());
        DigestUtils.md5("123".getBytes());
        System.out.println(DigestUtils.md2Hex("123".getBytes()));
        System.out.println(DigestUtils.md5Hex("123".getBytes()));

        printHex(DigestUtils.getSha1Digest().digest("123".getBytes()));
        printHex(DigestUtils.getShaDigest().digest("123".getBytes()));
        printHex(DigestUtils.getDigest("sha1").digest("123".getBytes()));
        printHex(DigestUtils.getDigest("sha-1").digest("123".getBytes()));
        DigestUtils.sha1("123".getBytes());
        DigestUtils.sha("123".getBytes());
        DigestUtils.sha1Hex("123".getBytes());
        DigestUtils.shaHex("123".getBytes());

        printHex(DigestUtils.getDigest("sha256").digest("123".getBytes())); //OK!
        printHex(DigestUtils.getDigest(getDigest()).digest("123".getBytes())); //Unknown
    }

    public static String getDigest() {
        return "sha512";
    }

    private static void printHex(byte[] bytes) {
        System.out.println(HexUtil.toString(bytes));
    }

}
