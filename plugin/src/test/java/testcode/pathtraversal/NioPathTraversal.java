package testcode.pathtraversal;

import java.nio.file.Paths;

public class NioPathTraversal {

    public void loadFile(String path) {
        Paths.get(path);
        Paths.get(path,"foo");
        Paths.get(path,"foo", "bar");
        Paths.get("foo", path);
        Paths.get("foo", "bar", path);

        Paths.get("foo");
        Paths.get("foo","bar");
        Paths.get("foo","bar", "allsafe");

    }
}
