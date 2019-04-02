package testcode.serial;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;
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

    public UserEntity deserializeObjectWithInheritance(InputStream receivedFile) throws IOException, ClassNotFoundException {
        ClassLoaderObjectInputStream in = new ClassLoaderObjectInputStream(getClass().getClassLoader(), receivedFile);
        try {
            return (UserEntity) in.readObject();
        }
        finally {
            in.close();
        }
    }
}
