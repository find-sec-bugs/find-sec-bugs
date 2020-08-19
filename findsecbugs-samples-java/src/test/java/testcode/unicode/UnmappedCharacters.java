package testcode.unicode;

import org.apache.commons.codec.Charsets;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.security.SecureRandom;

public class UnmappedCharacters {

    public static void main(String[] args) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte[] key = new byte[10];
        sr.nextBytes(key);

        unsafe(key);
        safe(key);
    }
    public static void unsafe(byte[] input) {
        System.out.println(new String(input, Charsets.UTF_8));
    }

    public static void safe(byte[] input) throws CharacterCodingException {
        CharsetDecoder decoder = Charsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT);
        CharBuffer decoded = decoder.decode(ByteBuffer.wrap(input));
        System.out.println(decoded.toString());
    }

}
