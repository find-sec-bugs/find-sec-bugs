package testcode;

import java.io.IOException;
import java.net.*;

/**
 * @author Tomas Polesovsky
 */
public class UrlConnectionSSRF {

    public static void testURL(String url) throws IOException {
        new URL(url).openConnection().connect();

        new URL("http://safe.com").openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(url, 8080))).connect();

        new URL(url).openConnection().getInputStream();

        new URL(url).openConnection().getLastModified();

        new URL(url).openStream();

        new URL(url).getContent();

        new URL(url).getContent(new Class[0]);
    }

    public static void testURI(String url) throws IOException, URISyntaxException {
        new URI(url).toURL().openConnection().connect();
    }
}
