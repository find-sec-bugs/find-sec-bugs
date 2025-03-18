package testcode.xxe.schema;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public abstract class SchemaFactoryConfigurationDataFlow {

    private static final String EXTERNAL_PROTOCOLS_DISABLED = "";
    
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

    public Schema createAndConfigureSchemaInUnknownMethod() throws SAXException,
            MalformedURLException {
        SchemaFactory schemaFactory = getUnknownSchemaFactoryWithSafeTagsFromConfigFile();

        URL schemaFile = URI.create("some-file.xsd").toURL();
        return schemaFactory.newSchema(schemaFile);
    }
    
    public abstract SchemaFactory getUnknownSchemaFactoryWithSafeTagsFromConfigFile();
    
}
