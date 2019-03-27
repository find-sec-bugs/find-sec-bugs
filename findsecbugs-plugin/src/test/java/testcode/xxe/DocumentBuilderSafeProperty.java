package testcode.xxe;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DocumentBuilderSafeProperty {

    public static File getInputFile() {
        return new File("C:/Code/evil.xml"); ///tmp/user/upload_123.xml
    }

    private static void print(Document doc) {
        NodeList nodeList = doc.getChildNodes();
        for(int i=0 ; i<nodeList.getLength() ;i++ ) {
            Node n = nodeList.item(i);
            System.out.println(n.getTextContent());
        }
    }

    public static void unsafeNoSpecialSettings() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }


    /**
     * Activate secure processing. (universal switch)
     */
    public static void safeSecureProcessing() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }

    /**
     * No DTD. No problem.
     */
    public static void safeDtdDisable() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }

     /**
     * This implementation allow DTD but disable all its dangerous features.
     * Not sure it can still do something useful with DTD ...
     */
    public static void safeManualConfiguration() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }

    /// The following four test cases are incomplete version of the previous configuration
    /// (Where all settings are cherry picked)

    public static void unsafeManualConfig1() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }

    public static void unsafeManualConfig2() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
        //dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }

    public static void unsafeManualConfig3() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
        //dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }

    public static void unsafeManualConfig4() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://xml.org/sax/features/external-general-entities",true);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",true);
        dbf.setXIncludeAware(false);
        //dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = db.parse(getInputFile());
        print(doc);
    }
    
    
    public static void main(String[] args) throws Exception {
        //unsafeNoSpecialSettings();
        //safeSecureProcessing();
        safeDtdDisable();
    }
}
