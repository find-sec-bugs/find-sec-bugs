package testcode.graph;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class WelcomeController {

    @GetMapping("/test")
    public String direct(@RequestParam("xml") String xml) throws Exception {
        XmlService.receiveXMLStream(xml);
        return "/test";
    }

    @GetMapping("/deep")
    public String deep(@RequestParam("xml") String xml) throws Exception {
        callMe(xml);
        return "/test";
    }

    public void callMe(String xml) throws IOException, SAXException, ParserConfigurationException {
        XmlService.receiveXMLStream(xml);
    }
}
