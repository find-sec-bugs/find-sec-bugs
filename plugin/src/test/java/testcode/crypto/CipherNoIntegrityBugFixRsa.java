package testcode.crypto;

import javax.crypto.Cipher;
import java.security.GeneralSecurityException;

/**
 * Sample provided by https://github.com/h3xstream/find-sec-bugs/issues/24 (minus Guice reference)
 */
public class CipherNoIntegrityBugFixRsa {
    private static final String JCE_PROVIDER_BOUNCY_CASTLE = "BC";

    private static final String CIPHER = "RSA/NONE/OAEPWithSHA256AndMGF1Padding";

    public void checkBouncyCastleIsInstalled() {
        try {
            Cipher.getInstance(CIPHER, JCE_PROVIDER_BOUNCY_CASTLE);
        } catch (GeneralSecurityException e) {
            System.err.println(e.getMessage());
        }
    }

}
