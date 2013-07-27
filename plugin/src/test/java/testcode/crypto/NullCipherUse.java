package testcode.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class NullCipherUse {

    public static void main(String[] args) throws Exception {

        byte[] pt = "AAAAAAAAAAAAAAAA".getBytes("UTF-8");

        Cipher expectedCihper = Cipher.getInstance("AES/CBC/NoPadding");
        Cipher doNothingCihper = new NullCipher();


        printHex(encryptWithCipher(expectedCihper, pt));
        printHex(encryptWithCipher(doNothingCihper, pt));
    }

    public static byte[] encryptWithCipher(Cipher cipher, byte[] value) throws KeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {

        //Key generation
        byte[] passkey = "BBBBBBBBBBBBBBBB".getBytes("UTF-8");
        Cipher expectedCihper = cipher;
        SecretKeySpec key = new SecretKeySpec(passkey, "AES");
        //Setting the key
        expectedCihper.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[expectedCihper.getBlockSize()]));

        return cipher.doFinal(value);
    }

    private static void printHex(byte[] resultBytes) {
        for (byte b : resultBytes) {
            System.out.print(Integer.toHexString(b & 0xFF));
        }
        System.out.println();
    }

}
