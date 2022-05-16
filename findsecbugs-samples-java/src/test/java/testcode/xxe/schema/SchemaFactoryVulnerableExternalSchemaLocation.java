package testcode.xxe.schema;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SchemaFactoryVulnerableExternalSchemaLocation {

    public static void main(final String[] args) throws SAXException, IOException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final String documentSource = "<?xml version=\"1.0\"?><schema xmlns=\"http://www.w3.org/2001/XMLSchema\"><include schemaLocation=\"file:///etc/passwd\"/></schema>";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(documentSource.getBytes(StandardCharsets.UTF_8));
        final StreamSource source = new StreamSource(inputStream);

        schemaFactory.newSchema(source);
    }
}
