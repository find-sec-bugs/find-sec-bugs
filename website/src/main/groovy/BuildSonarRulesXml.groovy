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
        "UNVALIDATED_REDIRECT",
        "ANDROID_EXTERNAL_FILE_ACCESS",
        "ANDROID_WORLD_WRITABLE"
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

        if(pattern.attribute("type").equals('CUSTOM_INJECTION')) { //Custom injection is not export for Sonar analysis
            return
        }

        rule(key: pattern.attribute("type"),
                priority: getSonarPriority(pattern.attribute("type"))) {

            name("Security - " + pattern.ShortDescription.text())
            configKey(pattern.attribute("type"))
            description(pattern.Details.text())

            //OWASP TOP 10 2013
            if (pattern.Details.text().toLowerCase().contains('injection') || pattern.Details.text().contains('A1-Injection')) {
                tag("owasp-a1")
                tag("injection")
            }
            if (pattern.Details.text().contains('A2-Broken_Authentication_and_Session_Management')) {
                tag("owasp-a2")
            }
            if (pattern.Details.text().contains('A3-Cross-Site_Scripting')) {
                tag("owasp-a3")
            }
            if (pattern.Details.text().contains('A4-Insecure_Direct_Object_References') || pattern.Details.text().contains('Path_Traversal')) {
                tag("owasp-a4")
            }
            if (pattern.Details.text().contains('A5-Security_Misconfiguration')) {
                tag("owasp-a5")
            }
            if (pattern.attribute('type').equals('HARD_CODE_PASSWORD') ||
                    pattern.attribute("type") in cryptoBugs ||
                    pattern.Details.text().contains('A6-Sensitive_Data_Exposure')) {
                tag("owasp-a6")
                tag("cryptography")
            }
            if (pattern.Details.text().contains('A7-Missing_Function_Level_Access_Control')) {
                tag("owasp-a7")
            }
            if (pattern.Details.text().toLowerCase().contains('A8-Cross-Site_Request_Forgery')) {
                tag("owasp-a8")
            }
            if (pattern.Details.text().toLowerCase().contains('A9-Using_Components_with_Known_Vulnerabilities')) {
                tag("owasp-a9")
            }
            if (pattern.Details.text().toLowerCase().contains('A10-Unvalidated_Redirects_and_Forwards')) {
                tag("owasp-a10")
            }

            //

            if (pattern.Details.text().toLowerCase().contains('wasc')) {
                tag("wasc")
            }
            if (pattern.Details.text().toLowerCase().contains('cwe')) {
                tag("cwe")
            }
            if (pattern.ShortDescription.text().toLowerCase().contains('android')) {
                tag("android")
            }
            tag("security")
        }
        //name: pattern.ShortDescription.text(),
        //  'description': pattern.Details.text(),
        // 'type':pattern.attribute("type")])

    }
}

