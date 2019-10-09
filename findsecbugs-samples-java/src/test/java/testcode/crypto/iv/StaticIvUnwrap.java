package testcode.crypto.iv;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StaticIvUnwrap {
    public Key extractSecretKey(
        String keyEncryptionAlgorithmId,
        byte[] keyEncryptionAlgorithmParameters,
        String contentEncryptionAlgorithmId,
        byte[] derivedKey,
        byte[] encryptedContentEncryptionKey
    ) throws Exception {
        Cipher keyEncryptionCipher = Cipher.getInstance(keyEncryptionAlgorithmId);

        IvParameterSpec ivSpec = new IvParameterSpec(keyEncryptionAlgorithmParameters);

        keyEncryptionCipher.init(Cipher.UNWRAP_MODE, new SecretKeySpec(derivedKey, keyEncryptionCipher.getAlgorithm()), ivSpec);

        return keyEncryptionCipher.unwrap(encryptedContentEncryptionKey, contentEncryptionAlgorithmId, Cipher.SECRET_KEY);
    }
}
