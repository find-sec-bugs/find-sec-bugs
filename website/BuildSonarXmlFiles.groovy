import groovy.text.SimpleTemplateEngine
import groovy.xml.MarkupBuilder



findBugsPatterns = ["XSS_REQUEST_PARAMETER_TO_SEND_ERROR",
                    "XSS_REQUEST_PARAMETER_TO_SERVLET_WRITER",
                    "HRS_REQUEST_PARAMETER_TO_HTTP_HEADER",
                    "HRS_REQUEST_PARAMETER_TO_COOKIE",
                    "DMI_CONSTANT_DB_PASSWORD",
                    "DMI_EMPTY_DB_PASSWORD",
                    "SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE",
                    "SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING",
]

informationnalPatterns = ["SERVLET_PARAMETER",
                          "SERVLET_CONTENT_TYPE",
                          "SERVLET_SERVER_NAME",
                          "SERVLET_SESSION_ID",
                          "SERVLET_QUERY_STRING",
                          "SERVLET_HEADER",
                          "SERVLET_HEADER_REFERER",
                          "SERVLET_HEADER_USER_AGENT",
                          "COOKIE_USAGE",
                          "WEAK_FILENAMEUTILS",
                          "JAXWS_ENDPOINT",
                          "JAXRS_ENDPOINT",
                          "TAPESTRY_ENDPOINT",
                          "WICKET_ENDPOINT",
                          "FILE_UPLOAD_FILENAME",
                          "STRUTS1_ENDPOINT",
                          "STRUTS2_ENDPOINT",
                          "SPRING_ENDPOINT",
                          "HTTP_RESPONSE_SPLITTING",
                          "CRLF_INJECTION_LOGS",
                          "EXTERNAL_CONFIG_CONTROL",
                          "STRUTS_FORM_VALIDATION",
                          "ESAPI_ENCRYPTOR",
                          "ANDROID_BROADCAST",
                          "ANDROID_GEOLOCATION",
                          "ANDROID_WEB_VIEW_JAVASCRIPT",
                          "ANDROID_WEB_VIEW_JAVASCRIPT_INTERFACE"]

cryptoBugs = [
        "WEAK_TRUST_MANAGER",
        "WEAK_HOSTNAME_VERIFIER",
        //"WEAK_MESSAGE_DIGEST", //Deprecated
        "WEAK_MESSAGE_DIGEST_MD5",
        "WEAK_MESSAGE_DIGEST_SHA1",
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

majorBugsAuditOnly = [ //Mostly due to their high false-positive rate
        "TRUST_BOUNDARY_VIOLATION"
]

majorBugs = [
        "PREDICTABLE_RANDOM",
        "PATH_TRAVERSAL_IN",
        "PATH_TRAVERSAL_OUT",
        "REDOS",
        "BAD_HEXA_CONVERSION",
        "HARD_CODE_PASSWORD",
        "HARD_CODE_KEY",
        "XSS_REQUEST_WRAPPER",
        "UNVALIDATED_REDIRECT",
        "ANDROID_EXTERNAL_FILE_ACCESS",
        "ANDROID_WORLD_WRITABLE",
        "INSECURE_COOKIE",
        "HTTPONLY_COOKIE",
        "TRUST_BOUNDARY_VIOLATION",
        "XSS_SERVLET",
]

criticalBugs = [ //RCE or powerful function
        "COMMAND_INJECTION",
        "XXE_SAXPARSER",
        "XXE_XMLREADER",
        "XXE_DOCUMENT",
        "SQL_INJECTION_HIBERNATE",
        "SQL_INJECTION_JDO",
        "SQL_INJECTION_JPA",
        "LDAP_INJECTION",
        "XPATH_INJECTION",
        "XML_DECODER",
        "SCRIPT_ENGINE_INJECTION",
        "SPEL_INJECTION",
        "SQL_INJECTION_SPRING_JDBC",
        "SQL_INJECTION_JDBC",
        "EL_INJECTION",
        "SEAM_LOG_INJECTION",
        "OBJECT_DESERIALIZATION",
        "MALICIOUS_XSLT"
]

majorJspBugs = ["XSS_REQUEST_PARAMETER_TO_JSP_WRITER",
        "XSS_JSP_PRINT", "JSP_JSTL_OUT"]

criticalJspBugs = ["JSP_INCLUDE","JSP_SPRING_EVAL","JSP_XSLT"]

exclusions = ['CUSTOM_INJECTION']

////////////// Generate rules files

def getSonarPriority(String type) {
    if (type in criticalBugs) return "CRITICAL";
    if (type in majorBugs || type in cryptoBugs) return "MAJOR";
    return "INFO";
}

def writeRules(String rulesSetName,List<String> includedBugs) {
    //Load the bug descriptions
    String metaDataDir = "../plugin/src/main/resources/metadata"
    InputStream messagesStream = new FileInputStream(new File(metaDataDir,"messages.xml"))
    messagesXml = new XmlParser().parse(messagesStream)

    File f = new File("out_sonar","rules-"+rulesSetName+".xml")
    printf("Building ruleset %s (%s)%n",rulesSetName,f.getCanonicalPath())

    def xml = new MarkupBuilder(new PrintWriter(f))

    xml.rules {
        mkp.comment "This file is auto-generated."

        messagesXml.BugPattern.each { pattern ->

            if(includedBugs.contains(pattern.attribute("type")))

            rule(key: pattern.attribute("type"),
                    priority: getSonarPriority(pattern.attribute("type"))) {

                name("Security - " + pattern.ShortDescription.text())
                configKey(pattern.attribute("type"))
                description(pattern.Details.text().trim())

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
}

writeRules("findsecbugs", informationnalPatterns + findBugsPatterns + cryptoBugs + majorBugs + criticalBugs)
writeRules("jsp",majorJspBugs + criticalJspBugs)

////////////// Generate the profile files

def writeProfile(String profileName,List<String> includedBugs) {
    //Load the bug descriptions
    String metaDataDir = "../plugin/src/main/resources/metadata"
    InputStream messagesStream = new FileInputStream(new File(metaDataDir,"messages.xml"))
    messagesXml = new XmlParser().parse(messagesStream)

    File f = new File("out_sonar","profile-"+profileName+".xml")
    printf("Building profile %s (%s)%n",profileName,f.getCanonicalPath())



    def xml = new MarkupBuilder(new PrintWriter(f))
    xml.FindBugsFilter {
        mkp.comment "This file is auto-generated."


        includedBugs.forEach { patternName ->

            Match {
                Bug(pattern: patternName)
            }
        }

    }
}

writeProfile("findbugs-security-audit", informationnalPatterns + cryptoBugs + majorBugs + majorBugsAuditOnly + criticalBugs + findBugsPatterns)
writeProfile("findbugs-security-minimal", cryptoBugs + majorBugs + criticalBugs + findBugsPatterns)
writeProfile("findbugs-security-jsp", majorJspBugs + criticalJspBugs)
