package testcode.xpath;

import org.apache.xml.security.utils.XPathAPI;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

public abstract class XPathApacheXmlSec {


    public void main(@RequestParam("test") String input) throws Exception {
        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

        XPathAPI api = getAPI();

        api.evaluate(null,null,query,null);
        api.selectNodeList(null,null,query,null);
    }

    public abstract XPathAPI getAPI();
}
