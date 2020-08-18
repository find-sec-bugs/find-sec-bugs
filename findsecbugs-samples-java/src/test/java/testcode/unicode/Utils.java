package testcode.unicode;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

/**
 * jdk.internal.net.http.common.Utils
 */
public class Utils {

    private static final char[] hexDigits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static void appendEscape(StringBuilder sb, byte b) {
        sb.append('%');
        sb.append(hexDigits[(b >> 4) & 0x0f]);
        sb.append(hexDigits[(b >> 0) & 0x0f]);
    }

    public static String encode(String s) {
        int n = s.length();
        if (n == 0)
            return s;

        // First check whether we actually need to encode
        for (int i = 0;;) {
            if (s.charAt(i) >= '\u0080')
                break;
            if (++i >= n)
                return s;
        }

        String ns = Normalizer.normalize(s, Normalizer.Form.NFC);
        ByteBuffer bb = null;
        try {
            bb = StandardCharsets.UTF_8.newEncoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .encode(CharBuffer.wrap(ns));
        } catch (CharacterCodingException x) {
            assert false : x;
        }

        StringBuilder sb = new StringBuilder();
        while (bb.hasRemaining()) {
            int b = bb.get() & 0xff;
            if (b >= 0x80)
                appendEscape(sb, (byte)b);
            else
                sb.append((char)b);
        }
        return sb.toString();
    }

}
