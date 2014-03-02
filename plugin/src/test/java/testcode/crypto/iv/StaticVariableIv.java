package testcode.crypto.iv;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import testcode.util.HexUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.util.Random;

public class StaticVariableIv {

    static Random r = new SecureRandom();

    //Static IV ? potential reuse over time ?
    static byte[] iv = new byte[16];

    public static void init() {
        Security.addProvider(new BouncyCastleProvider());
        r.nextBytes(iv);
    }


    public static void encrypt(String message) throws Exception {
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
