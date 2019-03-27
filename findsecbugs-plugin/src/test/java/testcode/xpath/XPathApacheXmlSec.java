package testcode.xpath;

import org.apache.xml.security.utils.JDKXPathAPI;
import org.apache.xml.security.utils.XPathAPI;
import org.apache.xml.security.utils.XalanXPathAPI;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

public abstract class XPathApacheXmlSec {


    public void main(@RequestParam("test") String input) throws Exception {
        Document doc = XmlUtils.loadDoc("/testcode/xpath/data.xml");

        String query = "//groups/group[@id='" + input + "']/writeAccess/text()";

        XPathAPI api1 = getXPathAPI();

        api1.evaluate(null,null,query,null);
        api1.selectNodeList(null,null,query,null);


        JDKXPathAPI api2 = getJDKXPathAPI();

        api2.evaluate(null,null,query,null);
        api2.selectNodeList(null,null,query,null);

        XalanXPathAPI api3 = getXalanXPathAPI();

        api3.evaluate(null,null,query,null);
        api3.selectNodeList(null,null,query,null);
    }

    public abstract XPathAPI getXPathAPI();
    public abstract JDKXPathAPI getJDKXPathAPI();
    public abstract XalanXPathAPI getXalanXPathAPI();
}
