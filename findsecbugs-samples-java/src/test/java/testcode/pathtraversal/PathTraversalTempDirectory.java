package testcode.pathtraversal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTraversalTempDirectory {
    public static void main(String input) throws IOException {
        Path p = Paths.get("/");

        Files.createTempFile(p,input,"");
        Files.createTempFile(p,"",input);
        Files.createTempFile(input,"");
        Files.createTempFile("", input);

        Files.createTempDirectory(p,input);
        Files.createTempDirectory(input);
    }
}

