package testcode.crypto.iv;

import testcode.util.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ConstantIv {

    public static void encryptIvNotInitialize1(String message) throws Exception {

        byte[] iv = new byte[16]; //Oups. All 0

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

    public static void encryptIvNotInitialize2(String message) throws Exception {

        //IV
        IvParameterSpec ivSpec = new IvParameterSpec(new byte[16]);  //Oups. All 0

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
