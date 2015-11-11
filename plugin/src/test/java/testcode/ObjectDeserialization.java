package testcode;

import java.io.*;

class ObjectInputSubclass extends ObjectInputStream {

    public ObjectInputSubclass(InputStream in) throws IOException {
        super(in);
    }
}
public class ObjectDeserialization {

    public void main(String[] mains) throws IOException, ClassNotFoundException {
        byte[] buf = {};
        ObjectInputStream input = new ObjectInputSubclass(new ByteArrayInputStream(buf));
        input.readObject();
    }
}
