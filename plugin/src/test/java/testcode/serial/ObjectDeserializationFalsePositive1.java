package testcode.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Why is this consider a false positive?
 *
 * Doing a readObject to fetch the next object in the stream is not really creating a new context of deserialization.
 * If sandboxing is needed, the custom classloader should be set when the initial deserialization is started.
 */
public class ObjectDeserializationFalsePositive1 implements Serializable {
    int param1;
    Object param2;

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        param1 = inputStream.readInt();
        param2 = inputStream.readObject();
    }
}
