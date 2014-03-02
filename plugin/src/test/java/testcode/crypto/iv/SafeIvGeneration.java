package testcode.crypto.iv;

import testcode.util.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class SafeIvGeneration {

    public static void encrypt(String message) throws Exception {

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        //IV
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        //Key
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey secretKey = generator.generateKey();

        //Encrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        cipher.update(message.getBytes());

        byte[] data = cipher.doFinal();
        System.out.println(HexUtil.toString(data));
    }
}
