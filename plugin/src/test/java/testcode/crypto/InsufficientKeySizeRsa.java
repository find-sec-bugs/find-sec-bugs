package testcode.crypto;

import java.security.*;
import java.security.spec.RSAKeyGenParameterSpec;

/**
 * The key size might need to be ajust in the future..
 * http://en.wikipedia.org/wiki/Key_size#Asymmetric_algorithm_key_lengths
 */
public class InsufficientKeySizeRsa {

    public KeyPair weakKeySize1() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512); //BAD

        return keyGen.generateKeyPair();
    }

    public KeyPair weakKeySize2() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(128, new SecureRandom()); //BAD //Different signature

        return keyGen.generateKeyPair();
    }


    public KeyPair weakKeySize3ParameterSpec() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(new RSAKeyGenParameterSpec(128,RSAKeyGenParameterSpec.F4)); //BAD

        KeyPair key = keyGen.generateKeyPair();
        return key;
    }

    public KeyPair weakKeySize4ParameterSpec() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(new RSAKeyGenParameterSpec(128,RSAKeyGenParameterSpec.F4), new SecureRandom()); //BAD

        KeyPair key = keyGen.generateKeyPair();
        return key;
    }

    public KeyPair okKeySize() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);

        return keyGen.generateKeyPair();
    }

    public KeyPair okKeySizeParameterSpec() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(new RSAKeyGenParameterSpec(2048,RSAKeyGenParameterSpec.F4)); //Different signature

        return keyGen.generateKeyPair();
    }


}
