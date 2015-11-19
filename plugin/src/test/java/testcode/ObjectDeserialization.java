package testcode;

import testcode.sqli.UserEntity;

import java.io.*;

public class ObjectDeserialization {


    public UserEntity deserializeObject(InputStream receivedFile) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(receivedFile);
        try {
            return (UserEntity) in.readObject();
        }
        finally {
            in.close();
        }
    }
}
