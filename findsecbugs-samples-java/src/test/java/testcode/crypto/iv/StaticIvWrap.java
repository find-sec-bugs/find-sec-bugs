package testcode.crypto.iv;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// This is a synthetic test case - we use an unsafe IV for decryption (safe) and for key
// wrapping (unsafe). This is hand-crafted to make sure that key wrapping is handled
// as encryption (and not ignored).
public class StaticIvWrap {
    public void syntheticTestCase(
        Cipher cipher,
        byte[] unsafeIv,
        Key key
    ) throws Exception {
        IvParameterSpec ivSpec = new IvParameterSpec(unsafeIv);

        // Safe
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        // Unsafe
        cipher.init(Cipher.WRAP_MODE, key);
    }
}
