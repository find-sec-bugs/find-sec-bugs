package testcode.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class InsufficientKeySizeBlowfish {

    public SecretKey weakKeySize1() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
        keyGen.init(64);

        SecretKey key = keyGen.generateKey();
        return key;
    }

    public SecretKey weakKeySize2() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
        keyGen.init(96, new SecureRandom());

        SecretKey key = keyGen.generateKey();
        return key;
    }

    //TODO Cover API init( AlgorithmParameterSpec, ..)

    public SecretKey okKeySize1() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
        keyGen.init(128, new SecureRandom());

        SecretKey key = keyGen.generateKey();
        return key;
    }
}
