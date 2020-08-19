package sun.security.provider;

import java.math.BigInteger;
import java.security.InvalidKeyException;

/**
 *
 */
public class DSAPublicKeyImpl {

    /**
     * Make a DSA public key from its DER encoding (X.509).
     */
    public DSAPublicKeyImpl(byte[] encoded) throws InvalidKeyException {

    }

    /**
     * Make a DSA public key out of a public key and three parameters.
     * The p, q, and g parameters may be null, but if so, parameters will need
     * to be supplied from some other source before this key can be used in
     * cryptographic operations.  PKIX RFC2459bis explicitly allows DSA public
     * keys without parameters, where the parameters are provided in the
     * issuer's DSA public key.
     *
     * @param y the actual key bits
     * @param p DSA parameter p, may be null if all of p, q, and g are null.
     * @param q DSA parameter q, may be null if all of p, q, and g are null.
     * @param g DSA parameter g, may be null if all of p, q, and g are null.
     */
    public DSAPublicKeyImpl(BigInteger y, BigInteger p, BigInteger q,
                            BigInteger g)
            throws InvalidKeyException {

    }

}
