import groovy.text.SimpleTemplateEngine
import groovy.xml.MarkupBuilder

//Contains the bug descriptions

findSecBugsXml = new XmlParser().parse(getClass().getResourceAsStream("/metadata/messages.xml"))

def xml = new MarkupBuilder(new PrintWriter(System.out))

findBugsPatterns = ["XSS_REQUEST_PARAMETER_TO_SEND_ERROR",
        "XSS_REQUEST_PARAMETER_TO_SERVLET_WRITER",
        "XSS_REQUEST_PARAMETER_TO_JSP_WRITER",
        "HRS_REQUEST_PARAMETER_TO_HTTP_HEADER",
        "HRS_REQUEST_PARAMETER_TO_COOKIE",
        "DMI_CONSTANT_DB_PASSWORD",
        "DMI_EMPTY_DB_PASSWORD",
        "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE",
        "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
]

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


exclusions = ['CUSTOM_INJECTION']

def includePattern(String type) {
    return (type in criticalBugs || type in majorBugs || type in cryptoBugs);
}

xml.FindBugsFilter {
    mkp.comment "This file is auto-generated."

    findSecBugsXml.BugPattern.each { pat ->

        type = pat.attribute("type")

        if(type in criticalBugs || type in majorBugs || type in cryptoBugs)
        //{
        if(!(type in exclusions))
            Match {
                Bug(pattern: pat.attribute("type"))
            }
        //}
    }


    findBugsPatterns.each { patName ->
        Match {
            Bug(pattern:patName)
        }
    }
}