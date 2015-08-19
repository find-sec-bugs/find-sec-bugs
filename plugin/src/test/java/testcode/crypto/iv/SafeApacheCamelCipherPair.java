package testcode.crypto.iv;



import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

/**
 * Code from "org.apache.camel.converter.stream.CipherPair"
 * This is the perfect example where the KeyGenerator provided the IV.
 */
public class SafeApacheCamelCipherPair {
    private final String transformation;
    private final Cipher enccipher;
    private final Cipher deccipher;

    public SafeApacheCamelCipherPair(String transformation)
            throws GeneralSecurityException {
        this.transformation = transformation;

        int d = transformation.indexOf('/');
        String cipherName;
        if (d > 0) {
            cipherName = transformation.substring(0, d);
        } else {
            cipherName = transformation;
        }
        KeyGenerator keygen = KeyGenerator.getInstance(cipherName);
        keygen.init(new SecureRandom());
        Key key = keygen.generateKey();
        this.enccipher = Cipher.getInstance(transformation);
        this.deccipher = Cipher.getInstance(transformation);
        this.enccipher.init(1, key);
        byte[] ivp = this.enccipher.getIV();
        this.deccipher.init(2, key, ivp == null ? null : new IvParameterSpec(ivp));
    }

    public String getTransformation() {
        return this.transformation;
    }

    public Cipher getEncryptor() {
        return this.enccipher;
    }

    public Cipher getDecryptor() {
        return this.deccipher;
    }
}
