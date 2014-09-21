package testcode.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/**
 * <b>Note on removal of the "TO-DO" for KeyGenerator.init(AlgorithmParameterSpec,...):</b><br/>
 *  KeyGenerator.init() doesn't seems to support a ParameterSpec specific to Blowfish.
 *  There are no "BlowfishParameterSpec" in the standard JRE.
 */
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

    public SecretKey okKeySize1() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("Blowfish");
        keyGen.init(128, new SecureRandom());

        SecretKey key = keyGen.generateKey();
        return key;
    }
}
