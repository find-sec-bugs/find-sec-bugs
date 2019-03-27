package testcode.crypto;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.EncryptionException;

import javax.crypto.SecretKey;

public class EsapiCrypto {
    //public static void main(String[] args) {
    //
    //}

    static void encryptMethods(SecretKey secretKey,PlainText pt) throws EncryptionException {

        ESAPI.encryptor().encrypt(pt);
        ESAPI.encryptor().encrypt(secretKey,pt);
        ESAPI.encryptor().encrypt("Encrypt me"); //ESAPI 2.0.1 and lower
    }

    static void decryptMethods(SecretKey secretKey) throws EncryptionException {
        CipherText ct = new CipherText();
        ESAPI.encryptor().decrypt(ct);
        ESAPI.encryptor().decrypt(secretKey,ct);
        ESAPI.encryptor().decrypt(""); //ESAPI 2.0.1 and lower
    }
}
