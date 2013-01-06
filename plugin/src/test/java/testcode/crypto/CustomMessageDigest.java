package testcode.crypto;

import testcode.util.HexUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CustomMessageDigest extends MessageDigest {

    private ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    protected CustomMessageDigest() {
        super("WEAK");
    }

    @Override
    protected void engineUpdate(byte input) {
        buffer.write(input);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        try {
            buffer.write(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected byte[] engineDigest() {
        byte[] content = buffer.toByteArray();
        return Arrays.copyOf(content, 8);
    }

    @Override
    protected void engineReset() {
        buffer.reset();
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {

        MessageDigest dig = new CustomMessageDigest();
        dig.update("This is a test!".getBytes());
        byte[] result = dig.digest();
        printHex(result);
    }

    private static void printHex(byte[] bytes) {
        System.out.println(HexUtil.toString(bytes));
    }
}
