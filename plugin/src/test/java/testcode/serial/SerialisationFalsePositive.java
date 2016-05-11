package testcode.serial;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SerialisationFalsePositive implements Serializable {
    int param1;
    Object param2;

    private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        param1 = inputStream.readInt();
        param2 = inputStream.readObject();
    }
}
