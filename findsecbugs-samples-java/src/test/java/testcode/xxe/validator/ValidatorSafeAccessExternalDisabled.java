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

public class ValidatorSafeAccessExternalDisabled {

    private static final String EXTERNAL_PROTOCOLS_DISABLED = "";

    public static void main(final String[] args) throws SAXException, IOException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Schema schema = schemaFactory.newSchema();
        final Validator validator = schema.newValidator();
        validator.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, EXTERNAL_PROTOCOLS_DISABLED);
        validator.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, EXTERNAL_PROTOCOLS_DISABLED);

        final String documentSource = "<?xml version=\"1.0\"?><!DOCTYPE document [ <!ENTITY entity SYSTEM \"file:///etc/passwd\"> ]><document>&entity;</document>";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(documentSource.getBytes(StandardCharsets.UTF_8));
        final StreamSource source = new StreamSource(inputStream);

        validator.validate(source);
    }
}
