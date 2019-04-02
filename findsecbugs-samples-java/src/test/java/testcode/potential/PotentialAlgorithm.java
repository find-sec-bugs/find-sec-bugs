package testcode.potential;

import javax.crypto.Cipher;
import java.util.Properties;

public class PotentialAlgorithm {

    public void testDesAlgo(Properties config) throws Exception {
        String algorithmWithBadDefault = config.getProperty("algorithm", "DES");
        Cipher.getInstance(algorithmWithBadDefault);
    }
}
