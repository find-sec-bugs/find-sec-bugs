package testcode.serial;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * New signature for ObjectInput. See https://github.com/find-sec-bugs/find-sec-bugs/issues/563
 */
public class ObjectInputSig {

    public static Object deserialize1(String testString) {
        ByteArrayInputStream byteStream =  new ByteArrayInputStream(testString.getBytes());
        try {
            ObjectInput input = new ObjectInputStream(byteStream);
            return (Object)input.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object deserialize2(String testString) {
        ByteArrayInputStream byteStream =  new ByteArrayInputStream(testString.getBytes());
        try {
            ObjectInputStream input = new ObjectInputStream(byteStream);
            return (Object)input.readObject();
        } catch (ClassNotFoundException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
