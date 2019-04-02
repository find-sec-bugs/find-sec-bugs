package testcode.graph;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainStart {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        String configXml = new String(Files.readAllBytes(Paths.get("/test")));
        XmlService.receiveXMLStream(configXml);
    }
}
