package testcode;

import static org.apache.commons.io.FilenameUtils.*;

import java.io.File;
import java.io.IOException;

public class WeakFilenameUtils {

    public static void main(String[] args) throws IOException {
        String maliciousPath = "/test%00/././../../././secret/note.cfg\u0000dummy.jpg";

        testPath(maliciousPath);
    }

    private static void testPath(String maliciousPath) throws IOException {
        String path = normalize(maliciousPath);
        System.out.println("Expected:" + path + " -> Actual:" + canonical(path));

        String extension = getExtension(maliciousPath);
        System.out.println("Expected:" + extension + " -> Actual:" + getExtension(canonical(path)));

        boolean isExtension = isExtension(maliciousPath, "jpg");
        System.out.println("Expected:" + isExtension + " -> Actual:" + isExtension(canonical(path), "jpg"));

        String name = getName(maliciousPath);
        System.out.println("Expected:" + name + " -> Actual:" + getName(canonical(name)));

        String baseName = getBaseName(maliciousPath);
        System.out.println("Expected:" + baseName + " -> Actual:" + getBaseName(canonical(baseName)));
    }

    private static String canonical(String path) throws IOException {
        return new File(path).getCanonicalPath();
    }
}
