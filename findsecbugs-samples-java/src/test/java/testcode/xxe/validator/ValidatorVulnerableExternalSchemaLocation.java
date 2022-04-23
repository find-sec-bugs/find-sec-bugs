package testcode.xxe.validator;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ValidatorVulnerableExternalSchemaLocation {

    public static void main(final String[] args) throws SAXException, IOException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema();
        final Validator validator = schema.newValidator();

        final String documentSource = "<?xml version=\"1.0\"?><document xmlns=\"urn:external\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:external file:///etc/passwd\" />";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(documentSource.getBytes(StandardCharsets.UTF_8));
        final StreamSource source = new StreamSource(inputStream);

        validator.validate(source);
    }
}
