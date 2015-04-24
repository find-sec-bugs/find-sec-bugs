import groovy.text.SimpleTemplateEngine
import groovy.xml.MarkupBuilder

//Contains the bug descriptions
InputStream messagesStream = getClass().getResourceAsStream("/metadata/messages.xml")

//Loading detectors
rootXml = new XmlParser().parse(messagesStream)

cryptoBugs = [
        "WEAK_TRUST_MANAGER",
        "WEAK_MESSAGE_DIGEST",
        "CUSTOM_MESSAGE_DIGEST",
        "HAZELCAST_SYMMETRIC_ENCRYPTION",
        "NULL_CIPHER",
        "UNENCRYPTED_SOCKET",
        "DES_USAGE",
        "RSA_NO_PADDING",
        "RSA_KEY_SIZE",
        "BLOWFISH_KEY_SIZE",
        "STATIC_IV",
        "ECB_MODE",
        "PADDING_ORACLE",
        "CIPHER_INTEGRITY"
]
majorBugs = [
        "PREDICTABLE_RANDOM",
        "PATH_TRAVERSAL_IN",
        "PATH_TRAVERSAL_OUT",
        "REDOS",
        "BAD_HEXA_CONVERSION",
        "HARD_CODE_PASSWORD",
        "XSS_REQUEST_WRAPPER",
        "UNVALIDATED_REDIRECT"
]
criticalBugs = [
        "COMMAND_INJECTION",
        "XXE_SAXPARSER",
        "XXE_XMLREADER",
        "XXE_DOCUMENT",
        "SQL_INJECTION_HIBERNATE",
        "SQL_INJECTION_JDO",
        "SQL_INJECTION_JPA",
        "LDAP_INJECTION",
        "XPATH_INJECTION",
        "XSS_JSP_PRINT",
        "XML_DECODER",
        "XSS_SERVLET",
        "SCRIPT_ENGINE_INJECTION",
        "SPEL_INJECTION"
]

def getSonarPriority(String type) {
    if (type in criticalBugs) return "CRITICAL";
    if (type in majorBugs || type in cryptoBugs) return "MAJOR";
    return "INFO";
}

def xml = new MarkupBuilder(new PrintWriter(System.out))


xml.rules {
    mkp.comment "This file is auto-generated."

    rootXml.BugPattern.each { pattern ->

        rule(key: pattern.attribute("type"),
                priority: getSonarPriority(pattern.attribute("type"))) {

            name("Security - " + pattern.ShortDescription.text())
            configKey(pattern.attribute("type"))
            description(pattern.Details.text())

            //
            if (pattern.Details.text().toLowerCase().contains('owasp') &&
                    pattern.Details.text().toLowerCase().contains('top 10')) {
                tag("owasp-top10")
            }
            if (pattern.Details.text().toLowerCase().contains('wasc')) {
                tag("wasc")
            }
            if (pattern.Details.text().toLowerCase().contains('cwe')) {
                tag("cwe")
            }
            if (pattern.ShortDescription.text().toLowerCase().contains('injection')) {
                tag("injection")
            }
            if (pattern.ShortDescription.text().toLowerCase().contains('android')) {
                tag("android")
            }
            if (pattern.attribute("type") in cryptoBugs) {
                tag("cryptography")
            }
            tag("security")
        }
        //name: pattern.ShortDescription.text(),
        //  'description': pattern.Details.text(),
        // 'type':pattern.attribute("type")])

    }
}

