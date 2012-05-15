package testcode;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class WeakFilenameUtils {

    public static void main(String[] args) throws IOException {
        String maliciousPath = "/test/test%00/././../../././secret/note.cfg\u0000dummy.jpg";

        testPath(maliciousPath);
    }

    private static void testPath(String maliciousPath) throws IOException {
        String path = FilenameUtils.normalize(maliciousPath);
        System.out.println("Expected:" + path + " -> Actual:" + canonical(path));

        String extension = FilenameUtils.getExtension(maliciousPath);
        System.out.println("Expected:" + extension + " -> Actual:" + FilenameUtils.getExtension(canonical(path)));

        boolean isExtension = FilenameUtils.isExtension(maliciousPath, "jpg");
        System.out.println("Expected:" + isExtension + " -> Actual:" + FilenameUtils.isExtension(canonical(path), "jpg"));

        String name = FilenameUtils.getName(maliciousPath);
        System.out.println("Expected:" + name + " -> Actual:" + FilenameUtils.getName(canonical(name)));

        String baseName = FilenameUtils.getBaseName(maliciousPath);
        System.out.println("Expected:" + baseName + " -> Actual:" + FilenameUtils.getBaseName(canonical(baseName)));
    }

    private static String canonical(String path) throws IOException {
        return new File(path).getCanonicalPath();
    }
}
