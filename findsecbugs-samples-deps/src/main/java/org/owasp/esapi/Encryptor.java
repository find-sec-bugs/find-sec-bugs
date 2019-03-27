package org.owasp.esapi;

import org.owasp.esapi.crypto.CipherText;
import org.owasp.esapi.crypto.PlainText;
import org.owasp.esapi.errors.EncryptionException;

import javax.crypto.SecretKey;

public interface Encryptor {
    void encrypt(PlainText pt) throws EncryptionException;

    void encrypt(SecretKey secretKey, PlainText pt) throws EncryptionException;

    void encrypt(String pt) throws EncryptionException;


    void decrypt(CipherText ct);

    void decrypt(SecretKey secretKey, CipherText ct);

    void decrypt(String ct);
}
