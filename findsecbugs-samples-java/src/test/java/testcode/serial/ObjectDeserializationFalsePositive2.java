package testcode.serial;

import org.bouncycastle.asn1.ASN1InputStream;

import java.io.IOException;

public class ObjectDeserializationFalsePositive2 {
    public static void main(String[] args) throws IOException {

        new ASN1InputStream().readObject();
    }
}
