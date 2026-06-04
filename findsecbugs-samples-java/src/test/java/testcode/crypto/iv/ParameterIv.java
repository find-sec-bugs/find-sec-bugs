package testcode.crypto.iv;

import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;

/**
 * Reproduction for issue #765.
 *
 * The IV is received as a method parameter (it is filled by a SecureRandom or
 * provided by a caller for decryption elsewhere). Building an IvParameterSpec or
 * GCMParameterSpec from a parameter array is not a static IV, so STATIC_IV must
 * not be reported here.
 */
public class ParameterIv {

    private String algorithmMode = "GCM";

    private String getAlgorithmMode() {
        return algorithmMode;
    }

    private AlgorithmParameterSpec getKeyParameters(byte[] iv) {
        if ("GCM".equals(getAlgorithmMode())) {
            return new GCMParameterSpec(128, iv, 0, iv.length);
        } else {
            return new IvParameterSpec(iv, 0, iv.length);
        }
    }

    public AlgorithmParameterSpec buildFromParameter(byte[] iv) {
        return getKeyParameters(iv);
    }
}
