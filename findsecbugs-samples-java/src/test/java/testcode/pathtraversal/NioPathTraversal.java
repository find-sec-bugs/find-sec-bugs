package testcode.pathtraversal;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class NioPathTraversal {

    public void loadFile(String path) throws URISyntaxException {
        Paths.get(path);
        Paths.get(path,"foo");
        Paths.get(path,"foo", "bar");
        Paths.get("foo", path);
        Paths.get("foo", "bar", path);

        Paths.get(new URI("file", path, null));
        Paths.get(new URI("file", path, "foo"));

        Paths.get("foo");
        Paths.get("foo","bar");
        Paths.get("foo","bar", "allsafe");
        Paths.get(new URI("file", "foo", null));
        Paths.get(new URI("file", "foo", "bar"));

    }
}
