package testcode.xxe.schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SchemaFactorySafeAccessExternalDisabled {

    private static final String EXTERNAL_PROTOCOLS_DISABLED = "";

    public static void main(final String[] args) throws SAXException, IOException {
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, EXTERNAL_PROTOCOLS_DISABLED);
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, EXTERNAL_PROTOCOLS_DISABLED);

        final String documentSource = "<?xml version=\"1.0\"?><!DOCTYPE schema [ <!ENTITY entity SYSTEM \"file:///etc/passwd\"> ]><schema>&entity;</schema>";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(documentSource.getBytes(StandardCharsets.UTF_8));
        final StreamSource source = new StreamSource(inputStream);

        schemaFactory.newSchema(source);
    }

    public static Schema createSchemaAndConfigureInMethod() throws SAXException,
            MalformedURLException {

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        safelyConfigureSchemaFactoryInMethod(schemaFactory);

        URL schemaFile = URI.create("some-file.xsd").toURL();
        return schemaFactory.newSchema(schemaFile);
    }

    private static void safelyConfigureSchemaFactoryInMethod(SchemaFactory schemaFactory) throws SAXNotSupportedException,
            SAXNotRecognizedException {
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, EXTERNAL_PROTOCOLS_DISABLED);
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, EXTERNAL_PROTOCOLS_DISABLED);
    }

    public static Schema createAncConfigureSchemaInMethod() throws SAXException,
            MalformedURLException {
        SchemaFactory schemaFactory = safelyCreateAndConfigureSchemaFactoryInMethod();

        URL schemaFile = URI.create("some-file.xsd").toURL();
        return schemaFactory.newSchema(schemaFile);
    }

    private static SchemaFactory safelyCreateAndConfigureSchemaFactoryInMethod() throws SAXNotSupportedException,
            SAXNotRecognizedException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, EXTERNAL_PROTOCOLS_DISABLED);
        schemaFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, EXTERNAL_PROTOCOLS_DISABLED);
        return schemaFactory;
    }
}
