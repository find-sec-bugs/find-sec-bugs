package testcode.crypto;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Random;

public class StaticIv {

    static Random r = new Random();

    public static void main(String[] args) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        //Static IV ? potential reuse over time ?
        final byte[] iv = new byte[16];
        //r.nextBytes(iv);

        //IV
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        //Key
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        SecretKey secretKey = generator.generateKey();

        //Encrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        cipher.update("Hide me !".getBytes());

        byte[] data = cipher.doFinal();
        System.out.println(Hex.encodeHex(data));
    }
}
