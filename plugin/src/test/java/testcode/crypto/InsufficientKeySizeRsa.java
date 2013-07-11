package testcode.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * http://www.hpenterprisesecurity.com/vulncat/en/vulncat/java/weak_encryption_insufficient_key_size.html
 */
public class InsufficientKeySizeRsa {

    public KeyPair weakKeySize1() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);

        KeyPair key = keyGen.generateKeyPair();
        return key;
    }

    public KeyPair weakKeySize2() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(128, new SecureRandom()); //Different signature

        KeyPair key = keyGen.generateKeyPair();
        return key;
    }

    //TODO Cover API init( AlgorithmParameterSpec, ..)

    public KeyPair okKeySize() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);

        KeyPair key = keyGen.generateKeyPair();
        return key;
    }


}
