package testcode.xsl;

import org.apache.commons.io.IOUtils;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StdXmlTransform {

    public static final String FORLDER = "/testcode/xsl/";

    public static void main(String[] args) throws Exception {
        new StdXmlTransform().xslt1SafeStaticResource();
        //new StdXmlTransform().xslt2UnsafeResource("xsl_evil.xsl");
    }

    public void xslt1SafeStaticResource() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(getClass().getResourceAsStream(FORLDER+"xsl_safe.xsl"));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(getClass().getResourceAsStream(FORLDER+"input.xml"));
        transformer.transform(text, new StreamResult(System.out));
    }

    public void xslt2UnsafeResource(String input) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(getClass().getResourceAsStream(input));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(getClass().getResourceAsStream(FORLDER+"input.xml"));
        transformer.transform(text, new StreamResult(System.out));
    }

    public void xslt3UnsafeResource(String input) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(getClass().getResourceAsStream(FORLDER+input));
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(getClass().getResourceAsStream(FORLDER+"input.xml"));
        transformer.transform(text, new StreamResult(System.out));
    }

    public void xslt4UnsafeResource(String input) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        FileInputStream in = null;
        try {
            in = new FileInputStream(FORLDER+input);
            Source xslt = new StreamSource(in);
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(getClass().getResourceAsStream(FORLDER+"input.xml"));
            transformer.transform(text, new StreamResult(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public void xslt5SafeResource() throws TransformerException {
        String input = "/safe.xsl";
        TransformerFactory factory = TransformerFactory.newInstance();
        FileInputStream in = null;
        try {
            in = new FileInputStream(FORLDER+input);
            Source xslt = new StreamSource(in);
            Transformer transformer = factory.newTransformer(xslt);

            Source text = new StreamSource(getClass().getResourceAsStream(FORLDER+"input.xml"));
            transformer.transform(text, new StreamResult(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
