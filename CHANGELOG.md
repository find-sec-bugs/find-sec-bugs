# Change Log

## [Unreleased](https://github.com/find-sec-bugs/find-sec-bugs/tree/HEAD)

[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.6.0...HEAD)

**Closed issues:**

- Run coveralls after each build [\#287](https://github.com/find-sec-bugs/find-sec-bugs/issues/287)

**Merged pull requests:**

- Version 1.6.0 to 1.7.0 [\#286](https://github.com/find-sec-bugs/find-sec-bugs/pull/286) ([h3xstream](https://github.com/h3xstream))

## [version-1.6.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.6.0) (2017-03-15)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.5.0...version-1.6.0)

**Implemented enhancements:**

- Unexpected deserialization with RestEasy/Jersey [\#198](https://github.com/find-sec-bugs/find-sec-bugs/issues/198)
- Turbine SQL Injection [\#238](https://github.com/find-sec-bugs/find-sec-bugs/issues/238)
- Detect hardcoded password in unknown API [\#231](https://github.com/find-sec-bugs/find-sec-bugs/issues/231)
- Malicious deserialization from LDAP entry [\#228](https://github.com/find-sec-bugs/find-sec-bugs/issues/228)
- \(Dev internal\) Validate the configuration files automatically [\#158](https://github.com/find-sec-bugs/find-sec-bugs/issues/158)
- Turbine SQL injections [\#253](https://github.com/find-sec-bugs/find-sec-bugs/pull/253) ([h3xstream](https://github.com/h3xstream))
- Adding overly permissive CORS policy detector [\#248](https://github.com/find-sec-bugs/find-sec-bugs/pull/248) ([plr0man](https://github.com/plr0man))
- LDAP improvements [\#278](https://github.com/find-sec-bugs/find-sec-bugs/pull/278) ([h3xstream](https://github.com/h3xstream))
- Add HTTP Parameter Pollution Injection Detector [\#267](https://github.com/find-sec-bugs/find-sec-bugs/pull/267) ([plr0man](https://github.com/plr0man))
- Add File Disclosure Injection detector [\#265](https://github.com/find-sec-bugs/find-sec-bugs/pull/265) ([plr0man](https://github.com/plr0man))
- Java source and target from 1.6 to 1.7 & API compatibility check [\#264](https://github.com/find-sec-bugs/find-sec-bugs/pull/264) ([ptamarit](https://github.com/ptamarit))
- Add JavaBeans Property Injection detector [\#263](https://github.com/find-sec-bugs/find-sec-bugs/pull/263) ([plr0man](https://github.com/plr0man))
- Add Insecure SMTP SSL detector [\#259](https://github.com/find-sec-bugs/find-sec-bugs/pull/259) ([plr0man](https://github.com/plr0man))
- SQL Injection \(CWE-89\) - Scala Slick & Scala Anorm injection detectors [\#254](https://github.com/find-sec-bugs/find-sec-bugs/pull/254) ([MaxNad](https://github.com/MaxNad))
- Add Url rewriting detector [\#252](https://github.com/find-sec-bugs/find-sec-bugs/pull/252) ([plr0man](https://github.com/plr0man))
- UNENCRYPTED\_SERVER\_SOCKET: use of java.net.ServerSocket [\#239](https://github.com/find-sec-bugs/find-sec-bugs/pull/239) ([edrdo](https://github.com/edrdo))
- Server Side Request Forgery \(CWE 918\) - Basic detector implementation [\#234](https://github.com/find-sec-bugs/find-sec-bugs/pull/234) ([MaxNad](https://github.com/MaxNad))

**Fixed bugs:**

- Out of bounds mutables in ... \(Assertion trigged\) [\#275](https://github.com/find-sec-bugs/find-sec-bugs/issues/275)
- Force encoding to UTF-8 on windows when generating micro-website [\#232](https://github.com/find-sec-bugs/find-sec-bugs/issues/232)
- Freemarker description fix [\#230](https://github.com/find-sec-bugs/find-sec-bugs/issues/230)
- Bug fix of detection of bad cipher modes of operation and minor improvements [\#271](https://github.com/find-sec-bugs/find-sec-bugs/pull/271) ([formanek](https://github.com/formanek))

**Closed issues:**

- Find-sec-bugs maven plugin failed to execute [\#274](https://github.com/find-sec-bugs/find-sec-bugs/issues/274)
- False negatives in detection of bad modes of operation [\#270](https://github.com/find-sec-bugs/find-sec-bugs/issues/270)
- findbugs not working with Sonarqube 6.1 [\#235](https://github.com/find-sec-bugs/find-sec-bugs/issues/235)
- Update JSP compiler  [\#279](https://github.com/find-sec-bugs/find-sec-bugs/issues/279)

**Merged pull requests:**

- Remove duplicated word in README [\#282](https://github.com/find-sec-bugs/find-sec-bugs/pull/282) ([jwilk](https://github.com/jwilk))
- Update JSP compiler [\#281](https://github.com/find-sec-bugs/find-sec-bugs/pull/281) ([h3xstream](https://github.com/h3xstream))
- Fix \#275 [\#277](https://github.com/find-sec-bugs/find-sec-bugs/pull/277) ([h3xstream](https://github.com/h3xstream))
- Add Format String Manipulation Injection Detector [\#266](https://github.com/find-sec-bugs/find-sec-bugs/pull/266) ([plr0man](https://github.com/plr0man))
- Travis improvements: batch mode and verify phase [\#262](https://github.com/find-sec-bugs/find-sec-bugs/pull/262) ([ptamarit](https://github.com/ptamarit))
- Add AWS Query Injection detector [\#260](https://github.com/find-sec-bugs/find-sec-bugs/pull/260) ([plr0man](https://github.com/plr0man))
- Fix false negatives in InsufficientKeySizeRsaDetector [\#257](https://github.com/find-sec-bugs/find-sec-bugs/pull/257) ([plr0man](https://github.com/plr0man))
- Fix false negative SHA in WeakMessageDigestDetector [\#255](https://github.com/find-sec-bugs/find-sec-bugs/pull/255) ([plr0man](https://github.com/plr0man))
- Persistent cookie detector [\#251](https://github.com/find-sec-bugs/find-sec-bugs/pull/251) ([plr0man](https://github.com/plr0man))
- Anonymous LDAP Bind detector [\#250](https://github.com/find-sec-bugs/find-sec-bugs/pull/250) ([plr0man](https://github.com/plr0man))
- Fix Maven warnings \(missing plugin version, relocation, proprietary API\) [\#247](https://github.com/find-sec-bugs/find-sec-bugs/pull/247) ([ptamarit](https://github.com/ptamarit))
- Adding ThreadLocalRandom detection [\#246](https://github.com/find-sec-bugs/find-sec-bugs/pull/246) ([plr0man](https://github.com/plr0man))
- Improve SpringMvcEndpointDetector by detecting new RequestMapping annotation shortcuts [\#244](https://github.com/find-sec-bugs/find-sec-bugs/pull/244) ([ptamarit](https://github.com/ptamarit))
- Update plugins \#279 [\#280](https://github.com/find-sec-bugs/find-sec-bugs/pull/280) ([h3xstream](https://github.com/h3xstream))
- Spring CSRF: Protection Disabled & Unrestricted RequestMapping [\#261](https://github.com/find-sec-bugs/find-sec-bugs/pull/261) ([ptamarit](https://github.com/ptamarit))
- \(internal\) Refactoring: Rename Summary to TaintConfig [\#258](https://github.com/find-sec-bugs/find-sec-bugs/pull/258) ([h3xstream](https://github.com/h3xstream))

## [version-1.5.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.5.0) (2016-10-06)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.6...version-1.5.0)

**Implemented enhancements:**

- Detect template usage \(template injection\) [\#227](https://github.com/find-sec-bugs/find-sec-bugs/issues/227)
- Reduce the number of FP related to Trust Boundary Violation [\#226](https://github.com/find-sec-bugs/find-sec-bugs/issues/226)
- XSS in Portlet [\#216](https://github.com/find-sec-bugs/find-sec-bugs/issues/216)
- How to set findsecbugs.taint.customconfigfile through gradle? [\#215](https://github.com/find-sec-bugs/find-sec-bugs/issues/215)
- Identify weak XML parser properties that could lead to XXE [\#209](https://github.com/find-sec-bugs/find-sec-bugs/issues/209)
- Scala : XSS in twirl template [\#207](https://github.com/find-sec-bugs/find-sec-bugs/issues/207)
- Scala: XSS in Play controller [\#206](https://github.com/find-sec-bugs/find-sec-bugs/issues/206)
- XML parsing vulnerable to XXE \(XMLReader\) shortage [\#191](https://github.com/find-sec-bugs/find-sec-bugs/issues/191)
- Path Traversal \(CWE 22\) - Scala Path Traversal injection sinks [\#223](https://github.com/find-sec-bugs/find-sec-bugs/pull/223) ([MaxNad](https://github.com/MaxNad))
- Sensitive data exposure \(CWE 200\) - Sensitive data exposure in cookies [\#221](https://github.com/find-sec-bugs/find-sec-bugs/pull/221) ([MaxNad](https://github.com/MaxNad))
- XSS \(CWE 79\) - Scala - The detector can be fooled when the .as\("text/html"\) is in uppercase [\#208](https://github.com/find-sec-bugs/find-sec-bugs/pull/208) ([MaxNad](https://github.com/MaxNad))
- Taint analysis bug fixes and improvements [\#214](https://github.com/find-sec-bugs/find-sec-bugs/pull/214) ([topolik](https://github.com/topolik))
- Potential fix for issue \#182 \(INSECURE\_COOKIE detector can be fooled by creating two or more cookies\) [\#204](https://github.com/find-sec-bugs/find-sec-bugs/pull/204) ([MaxNad](https://github.com/MaxNad))
- XSS \(CWE 79\) - Scala Play vulnerable code [\#203](https://github.com/find-sec-bugs/find-sec-bugs/pull/203) ([MaxNad](https://github.com/MaxNad))
- CWE 200 \(Information Exposure\) - Scala Play vulnerable code [\#202](https://github.com/find-sec-bugs/find-sec-bugs/pull/202) ([MaxNad](https://github.com/MaxNad))

**Fixed bugs:**

- FP: sending local broadcasts via LocalBroadcastManager [\#224](https://github.com/find-sec-bugs/find-sec-bugs/issues/224)
- False positive: ResourceBundle in JSP [\#213](https://github.com/find-sec-bugs/find-sec-bugs/issues/213)
- Out of bounds mutables in static myclass$.\<clinit\>\(\)V [\#199](https://github.com/find-sec-bugs/find-sec-bugs/issues/199)
- Issue \#224 - Added an exception for the LocalBroadcastManager in the detector. [\#225](https://github.com/find-sec-bugs/find-sec-bugs/pull/225) ([MaxNad](https://github.com/MaxNad))
- Potential fix for issue \\#182 \\(INSECURE\\_COOKIE detector can be fooled by creating two or more cookies\\) [\#204](https://github.com/find-sec-bugs/find-sec-bugs/pull/204) ([MaxNad](https://github.com/MaxNad))

**Closed issues:**

- not to report null-porter dereference if there is code already throws RuntimeError [\#197](https://github.com/find-sec-bugs/find-sec-bugs/issues/197)
- Release version 1.4.6 [\#195](https://github.com/find-sec-bugs/find-sec-bugs/issues/195)
- Release 1.4.5 [\#159](https://github.com/find-sec-bugs/find-sec-bugs/issues/159)
- Fix mix-content on micro-website [\#229](https://github.com/find-sec-bugs/find-sec-bugs/issues/229)

**Merged pull requests:**

- Custom config file method refactoring [\#218](https://github.com/find-sec-bugs/find-sec-bugs/pull/218) ([topolik](https://github.com/topolik))
- Accept environment variables spelled with underscores [\#217](https://github.com/find-sec-bugs/find-sec-bugs/pull/217) ([kuhnmi](https://github.com/kuhnmi))

## [version-1.4.6](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.6) (2016-06-02)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.5...version-1.4.6)

**Implemented enhancements:**

- Detect deserialization gadgets [\#189](https://github.com/find-sec-bugs/find-sec-bugs/issues/189)
- CustomInjection issues [\#172](https://github.com/find-sec-bugs/find-sec-bugs/issues/172)
- New Rule : XSLT processing detection [\#168](https://github.com/find-sec-bugs/find-sec-bugs/issues/168)
- Better sink confirmation mechanism, safe fields [\#173](https://github.com/find-sec-bugs/find-sec-bugs/pull/173) ([formanek](https://github.com/formanek))
- Credentials detector for Hashtable improved [\#155](https://github.com/find-sec-bugs/find-sec-bugs/pull/155) ([mcwww](https://github.com/mcwww))
- Update owasp.txt [\#188](https://github.com/find-sec-bugs/find-sec-bugs/pull/188) ([s-tikhomirov](https://github.com/s-tikhomirov))
- Correct japanese messages formatting [\#185](https://github.com/find-sec-bugs/find-sec-bugs/pull/185) ([marcosbento](https://github.com/marcosbento))
- Support for sanitization using replace methods in String [\#171](https://github.com/find-sec-bugs/find-sec-bugs/pull/171) ([formanek](https://github.com/formanek))
- Taint tags for injections, proper tag derivation, added and fixed summaries [\#169](https://github.com/find-sec-bugs/find-sec-bugs/pull/169) ([formanek](https://github.com/formanek))
- Taint tags - support for taint sanitization \(starting with XSS\) [\#166](https://github.com/find-sec-bugs/find-sec-bugs/pull/166) ([formanek](https://github.com/formanek))
- Fix typo in taint-config/java-lang.txt [\#157](https://github.com/find-sec-bugs/find-sec-bugs/pull/157) ([apasel422](https://github.com/apasel422))

**Fixed bugs:**

- find-sec-bugs always claims "The following classes needed for analysis were missing" for enums  [\#176](https://github.com/find-sec-bugs/find-sec-bugs/issues/176)
- Memory leak in the tests [\#193](https://github.com/find-sec-bugs/find-sec-bugs/issues/193)
- Test failure : Invalid VNA after location [\#192](https://github.com/find-sec-bugs/find-sec-bugs/issues/192)
- java.util.ConcurrentModificationException during analysis [\#184](https://github.com/find-sec-bugs/find-sec-bugs/issues/184)
- CustomInjection issues [\#172](https://github.com/find-sec-bugs/find-sec-bugs/issues/172)
- FindSecBugs plugin crash in Intellij [\#167](https://github.com/find-sec-bugs/find-sec-bugs/issues/167)
- Fixed exception, debug info to visitGETFIELD, formatting [\#156](https://github.com/find-sec-bugs/find-sec-bugs/pull/156) ([formanek](https://github.com/formanek))

**Closed issues:**

- No plugin support for findbugs4sbt [\#181](https://github.com/find-sec-bugs/find-sec-bugs/issues/181)
- Fixing the build [\#180](https://github.com/find-sec-bugs/find-sec-bugs/issues/180)
- Standalone execution [\#179](https://github.com/find-sec-bugs/find-sec-bugs/issues/179)
- AbstractInjectionDetector.checkTaintSink\(\) modifies Set\<TaintSink\> while iterating over it [\#177](https://github.com/find-sec-bugs/find-sec-bugs/issues/177)
- Make the test less verbose [\#194](https://github.com/find-sec-bugs/find-sec-bugs/issues/194)

**Merged pull requests:**

- Safe enums, dates, time and context path + javadoc [\#190](https://github.com/find-sec-bugs/find-sec-bugs/pull/190) ([formanek](https://github.com/formanek))
- New analysis parameters and extended taint config [\#187](https://github.com/find-sec-bugs/find-sec-bugs/pull/187) ([formanek](https://github.com/formanek))
- Add Struts DynaValidatorForm support in addition to ValidatorForm [\#178](https://github.com/find-sec-bugs/find-sec-bugs/pull/178) ([mkienenb](https://github.com/mkienenb))
- Fix URL shown for CUSTOM\_INJECTION bug warning [\#174](https://github.com/find-sec-bugs/find-sec-bugs/pull/174) ([mkienenb](https://github.com/mkienenb))

## [version-1.4.5](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.5) (2016-01-05)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.4...version-1.4.5)

**Implemented enhancements:**

- Play framework demo [\#154](https://github.com/find-sec-bugs/find-sec-bugs/issues/154)
- New Rule : Scala Command injection [\#153](https://github.com/find-sec-bugs/find-sec-bugs/issues/153)
- New Rule : Unvalidated redirect in Play Framework [\#152](https://github.com/find-sec-bugs/find-sec-bugs/issues/152)
- New Rule : Additional coverage for predictable random generator in Scala [\#151](https://github.com/find-sec-bugs/find-sec-bugs/issues/151)
- New Rule: Detect weak HostnameVerifier [\#150](https://github.com/find-sec-bugs/find-sec-bugs/issues/150)
- Migrate the old XSS detector to the new TaintDetector mecanism [\#149](https://github.com/find-sec-bugs/find-sec-bugs/issues/149)
- Support alternative bytecode for setEscapeXml="false" JSP \(Weblogic appc\) [\#148](https://github.com/find-sec-bugs/find-sec-bugs/issues/148)
- \(Dev internal\) DSL for more intuitive method matching [\#147](https://github.com/find-sec-bugs/find-sec-bugs/issues/147)
- New Rule : Missing HttpOnly flag on cookie [\#144](https://github.com/find-sec-bugs/find-sec-bugs/issues/144)
- New Rule : Trust Boundary Violation [\#133](https://github.com/find-sec-bugs/find-sec-bugs/issues/133)
- Taint analysis : Add taint parameters annotate \(RequestParam, PathVariable, ..\) [\#132](https://github.com/find-sec-bugs/find-sec-bugs/issues/132)
- New Rule : EL Expression Injection [\#130](https://github.com/find-sec-bugs/find-sec-bugs/issues/130)
- New Rule : XSS detector using the taint detector approach [\#129](https://github.com/find-sec-bugs/find-sec-bugs/issues/129)
- \(Dev internal\) Debug info for taint value to allow troubleshooting of the stack [\#81](https://github.com/find-sec-bugs/find-sec-bugs/issues/81)
- New Rule : Seam Logger usage could lead to remote code execution [\#56](https://github.com/find-sec-bugs/find-sec-bugs/issues/56)
- New Rule: Detect SSL disabler \(Java  + Scala implementation\) [\#34](https://github.com/find-sec-bugs/find-sec-bugs/issues/34)
- Change description of cryptography plus bad grammar [\#146](https://github.com/find-sec-bugs/find-sec-bugs/pull/146) ([mcwww](https://github.com/mcwww))
- Correct SonarQube product name [\#142](https://github.com/find-sec-bugs/find-sec-bugs/pull/142) ([agabrys](https://github.com/agabrys))
- Analysis of indirect subclasses of HttpServlet for XSS [\#137](https://github.com/find-sec-bugs/find-sec-bugs/pull/137) ([formanek](https://github.com/formanek))

**Fixed bugs:**

- Fix code bloc in description for multiples Bug Patterns : JSP\_INCLUDE, JSP\_SPRING\_EVAL and JSP\_JSTL\_OUT [\#131](https://github.com/find-sec-bugs/find-sec-bugs/issues/131)
- Hard coded keys false positive when loading bytes from FileInputStream [\#126](https://github.com/find-sec-bugs/find-sec-bugs/issues/126)
- Description for weak digest need an update [\#119](https://github.com/find-sec-bugs/find-sec-bugs/issues/119)
- Error scanning Scala code in IntelliJ [\#112](https://github.com/find-sec-bugs/find-sec-bugs/issues/112)

**Merged pull requests:**

- Change to description [\#145](https://github.com/find-sec-bugs/find-sec-bugs/pull/145) ([mcwww](https://github.com/mcwww))
- Properly handle paths to files [\#136](https://github.com/find-sec-bugs/find-sec-bugs/pull/136) ([jsotuyod](https://github.com/jsotuyod))
- Fixed hard coded keys detector and out-of-bounds index in TaintAnalysis [\#135](https://github.com/find-sec-bugs/find-sec-bugs/pull/135) ([formanek](https://github.com/formanek))

## [version-1.4.4](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.4) (2015-11-20)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.3...version-1.4.4)

**Implemented enhancements:**

- Path traversal and Xpath injection detectors should use taint analysis [\#97](https://github.com/find-sec-bugs/find-sec-bugs/issues/97)
- Detector for external control of configuration \(CWE-15\) [\#124](https://github.com/find-sec-bugs/find-sec-bugs/issues/124)
- Detector for CRLF injection in logs \(CWE-117\) [\#123](https://github.com/find-sec-bugs/find-sec-bugs/issues/123)
- Detector for HTTP response splitting [\#121](https://github.com/find-sec-bugs/find-sec-bugs/issues/121)
- New Rule : JSTL out escapeXml=false [\#114](https://github.com/find-sec-bugs/find-sec-bugs/issues/114)
- Improvements for JSP support [\#110](https://github.com/find-sec-bugs/find-sec-bugs/issues/110)
- Add taint sinks for XPath injection [\#108](https://github.com/find-sec-bugs/find-sec-bugs/issues/108)
- Missing taint sinks for LDAP Injection [\#105](https://github.com/find-sec-bugs/find-sec-bugs/issues/105)
- New rule : Detect dynamic JSP Includes [\#104](https://github.com/find-sec-bugs/find-sec-bugs/issues/104)
- Standalone command line tool to scan jars with or without the source [\#100](https://github.com/find-sec-bugs/find-sec-bugs/issues/100)
- Better support for collections [\#99](https://github.com/find-sec-bugs/find-sec-bugs/issues/99)
- Consider inheritance for method summaries [\#98](https://github.com/find-sec-bugs/find-sec-bugs/issues/98)
- Refactor injection detectors [\#96](https://github.com/find-sec-bugs/find-sec-bugs/issues/96)
- New Rule : Detect Spring Eval JSP taglib [\#55](https://github.com/find-sec-bugs/find-sec-bugs/issues/55)
- Add detector for java object deserialization [\#127](https://github.com/find-sec-bugs/find-sec-bugs/pull/127) ([minlex](https://github.com/minlex))

**Fixed bugs:**

- Path traversal false positives [\#113](https://github.com/find-sec-bugs/find-sec-bugs/issues/113)

**Closed issues:**

- mvn compile failing after adding findsecbugs-plugin [\#128](https://github.com/find-sec-bugs/find-sec-bugs/issues/128)
- Add methods for weak message digest [\#120](https://github.com/find-sec-bugs/find-sec-bugs/issues/120)
- How can I mark / exclude false positives? [\#116](https://github.com/find-sec-bugs/find-sec-bugs/issues/116)
- Missing taint sinks for Spring SQL injection [\#109](https://github.com/find-sec-bugs/find-sec-bugs/issues/109)
- Method arguments are not tainted if their derived summary is stored  [\#106](https://github.com/find-sec-bugs/find-sec-bugs/issues/106)
- Push release 1.4.3 to upstream projects [\#101](https://github.com/find-sec-bugs/find-sec-bugs/issues/101)

**Merged pull requests:**

- CRLF in loggers and taint analysis improvements [\#125](https://github.com/find-sec-bugs/find-sec-bugs/pull/125) ([formanek](https://github.com/formanek))
- Response splitting, hash functions and messages [\#122](https://github.com/find-sec-bugs/find-sec-bugs/pull/122) ([formanek](https://github.com/formanek))
- Refactored and fixed injection detectors [\#115](https://github.com/find-sec-bugs/find-sec-bugs/pull/115) ([formanek](https://github.com/formanek))
- Inheritance aware taint analysis, extended collections support [\#107](https://github.com/find-sec-bugs/find-sec-bugs/pull/107) ([formanek](https://github.com/formanek))
- Fix injection copy. [\#102](https://github.com/find-sec-bugs/find-sec-bugs/pull/102) ([mweiden](https://github.com/mweiden))

## [version-1.4.3](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.3) (2015-09-16)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.2...version-1.4.3)

**Implemented enhancements:**

- All Runtime.exec methods should be taint sinks [\#92](https://github.com/find-sec-bugs/find-sec-bugs/issues/92)
- Add coverage for LDAP injection [\#89](https://github.com/find-sec-bugs/find-sec-bugs/issues/89)
- Improve the detection of weak message digest [\#88](https://github.com/find-sec-bugs/find-sec-bugs/issues/88)
- Improve the detection in the use of old ciphers [\#87](https://github.com/find-sec-bugs/find-sec-bugs/issues/87)
- Insecure cookie [\#86](https://github.com/find-sec-bugs/find-sec-bugs/issues/86)
- Spring JDBC API [\#74](https://github.com/find-sec-bugs/find-sec-bugs/issues/74)
- JDBC api coverage [\#73](https://github.com/find-sec-bugs/find-sec-bugs/issues/73)
- False positive on Static IV when using Cipher.getIv\(\) [\#62](https://github.com/find-sec-bugs/find-sec-bugs/issues/62)
- Improved taint analysis \(several bugs fixed, refactoring\) [\#91](https://github.com/find-sec-bugs/find-sec-bugs/pull/91) ([formanek](https://github.com/formanek))

**Fixed bugs:**

- Parametric taint state not changed when used as an argument of an unknown method [\#90](https://github.com/find-sec-bugs/find-sec-bugs/issues/90)
- Bad method summaries derived for complex flow [\#85](https://github.com/find-sec-bugs/find-sec-bugs/issues/85)
- Invalid taint modifications of local variables, when loaded from method summary [\#84](https://github.com/find-sec-bugs/find-sec-bugs/issues/84)
- Taint not transfered in chained call of StringBuilder.append [\#83](https://github.com/find-sec-bugs/find-sec-bugs/issues/83)
- Too many iterations bug [\#82](https://github.com/find-sec-bugs/find-sec-bugs/issues/82)
- Issue with constructor with List and array as parameter \(Command injection detection\) [\#80](https://github.com/find-sec-bugs/find-sec-bugs/issues/80)
- Fix DES detection [\#79](https://github.com/find-sec-bugs/find-sec-bugs/issues/79)
- EntityManager createQuery trips SECSQLIJPA even with safe usage [\#76](https://github.com/find-sec-bugs/find-sec-bugs/issues/76)
- The IV generation should only be verified for the encryption mode [\#64](https://github.com/find-sec-bugs/find-sec-bugs/issues/64)

**Merged pull requests:**

- Fixed incomplete candidate method for LDAP injections [\#94](https://github.com/find-sec-bugs/find-sec-bugs/pull/94) ([formanek](https://github.com/formanek))
- Added command injection sinks and CWE identifiers [\#93](https://github.com/find-sec-bugs/find-sec-bugs/pull/93) ([formanek](https://github.com/formanek))
- Unknown methods made to modify taint state of their parameters to unknown [\#78](https://github.com/find-sec-bugs/find-sec-bugs/pull/78) ([formanek](https://github.com/formanek))
- Global taint analysis, improvements and bug fixes [\#75](https://github.com/find-sec-bugs/find-sec-bugs/pull/75) ([formanek](https://github.com/formanek))

## [version-1.4.2](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.2) (2015-08-18)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.1...version-1.4.2)

**Implemented enhancements:**

- Improve taint analysis to avoid SQL Injection detected when StringBuilder is used [\#14](https://github.com/find-sec-bugs/find-sec-bugs/issues/14)

**Fixed bugs:**

- Remove slash from XXE short message [\#68](https://github.com/find-sec-bugs/find-sec-bugs/issues/68)

**Merged pull requests:**

- Refactoring of classes for taint analysis [\#71](https://github.com/find-sec-bugs/find-sec-bugs/pull/71) ([formanek](https://github.com/formanek))
- Translate a message of HARD\_CODE\_KEY pattern. [\#70](https://github.com/find-sec-bugs/find-sec-bugs/pull/70) ([naokikimura](https://github.com/naokikimura))
- Taint sources locations added to bug reports [\#69](https://github.com/find-sec-bugs/find-sec-bugs/pull/69) ([formanek](https://github.com/formanek))
- Separated hard coded password and key reporting [\#67](https://github.com/find-sec-bugs/find-sec-bugs/pull/67) ([formanek](https://github.com/formanek))
- Taint sources and improved taint transfer [\#66](https://github.com/find-sec-bugs/find-sec-bugs/pull/66) ([formanek](https://github.com/formanek))
- Improved hardcoded passwords and key detector + taint analysis [\#63](https://github.com/find-sec-bugs/find-sec-bugs/pull/63) ([formanek](https://github.com/formanek))
- Allow analyze to set classpath entries [\#60](https://github.com/find-sec-bugs/find-sec-bugs/pull/60) ([mbmihura](https://github.com/mbmihura))
- website: corrected typos [\#59](https://github.com/find-sec-bugs/find-sec-bugs/pull/59) ([obilodeau](https://github.com/obilodeau))

## [version-1.4.1](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.1) (2015-05-30)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.4.0...version-1.4.1)

**Implemented enhancements:**

- Detector hard coded Spring OAuth secret key [\#57](https://github.com/find-sec-bugs/find-sec-bugs/issues/57)
- Add CWE references to messages \(few missing\) [\#52](https://github.com/find-sec-bugs/find-sec-bugs/issues/52)
- Create a tutorial for IntelliJ IDE [\#51](https://github.com/find-sec-bugs/find-sec-bugs/issues/51)
- Create a japanese page on the micro-website for the bug patterns [\#50](https://github.com/find-sec-bugs/find-sec-bugs/issues/50)
- NetBeans tutorial [\#45](https://github.com/find-sec-bugs/find-sec-bugs/issues/45)
- Update the documentation for Sonar Qube [\#44](https://github.com/find-sec-bugs/find-sec-bugs/issues/44)
- ECB and no integrity detection + tests [\#53](https://github.com/find-sec-bugs/find-sec-bugs/pull/53) ([formanek](https://github.com/formanek))
- Detector for hard coded passwords and cryptographic keys [\#46](https://github.com/find-sec-bugs/find-sec-bugs/pull/46) ([formanek](https://github.com/formanek))

**Fixed bugs:**

- XXE - reader False Positive [\#47](https://github.com/find-sec-bugs/find-sec-bugs/issues/47)
- Fix URLs in messages.xml [\#43](https://github.com/find-sec-bugs/find-sec-bugs/issues/43)
- CustomInjectionSource.properties not found [\#42](https://github.com/find-sec-bugs/find-sec-bugs/issues/42)

**Merged pull requests:**

- Update messages\_ja.xml [\#49](https://github.com/find-sec-bugs/find-sec-bugs/pull/49) ([naokikimura](https://github.com/naokikimura))

## [version-1.4.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.4.0) (2015-04-03)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.3.1...version-1.4.0)

**Implemented enhancements:**

- Support java 8 - upgrade to findbugs 3.0.0 or higher. [\#37](https://github.com/find-sec-bugs/find-sec-bugs/issues/37)
- New Android Security detectors [\#39](https://github.com/find-sec-bugs/find-sec-bugs/issues/39)
- Move command injection to the main injection detector mecanism [\#33](https://github.com/find-sec-bugs/find-sec-bugs/issues/33)
- Create messages\_ja.xml [\#38](https://github.com/find-sec-bugs/find-sec-bugs/pull/38) ([naokikimura](https://github.com/naokikimura))
- Enable additional signatures to detector of injection [\#36](https://github.com/find-sec-bugs/find-sec-bugs/pull/36) ([naokikimura](https://github.com/naokikimura))

## [version-1.3.1](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.3.1) (2015-02-23)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.3.0...version-1.3.1)

**Implemented enhancements:**

- Add supports for the new URL specification for bug reference [\#35](https://github.com/find-sec-bugs/find-sec-bugs/issues/35)
- Higher priority for injections [\#32](https://github.com/find-sec-bugs/find-sec-bugs/issues/32)
- Remove ESAPI references in messages [\#31](https://github.com/find-sec-bugs/find-sec-bugs/issues/31)
- XXE - Separate guidelines \(XMLReader/SaxParser/DocumentParser\) [\#27](https://github.com/find-sec-bugs/find-sec-bugs/issues/27)
- XXE - Avoid false positive when secure features are set. [\#26](https://github.com/find-sec-bugs/find-sec-bugs/issues/26)
- Fix links in the descriptions [\#25](https://github.com/find-sec-bugs/find-sec-bugs/issues/25)
- JDO Query - Potential Injections [\#23](https://github.com/find-sec-bugs/find-sec-bugs/issues/23)
- JDO PersistenceManager - Potential Injections [\#22](https://github.com/find-sec-bugs/find-sec-bugs/issues/22)
- Hibernate Restrictions API - Potential Injections [\#21](https://github.com/find-sec-bugs/find-sec-bugs/issues/21)

**Fixed bugs:**

- MethodUnprofitableException throwing could be suppressed [\#29](https://github.com/find-sec-bugs/find-sec-bugs/issues/29)
- Fix links in the descriptions [\#25](https://github.com/find-sec-bugs/find-sec-bugs/issues/25)
- CipherWithNoIntegrityDetector throws exception on algorithm-only cipher lookups [\#24](https://github.com/find-sec-bugs/find-sec-bugs/issues/24)
- Copy all files in metadata folder [\#30](https://github.com/find-sec-bugs/find-sec-bugs/pull/30) ([jsotuyod](https://github.com/jsotuyod))

## [version-1.3.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.3.0) (2015-01-02)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.2.1...version-1.3.0)

**Implemented enhancements:**

- Tag 1.2.1 release [\#18](https://github.com/find-sec-bugs/find-sec-bugs/issues/18)

## [version-1.2.1](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.2.1) (2014-10-03)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.2.0...version-1.2.1)

**Implemented enhancements:**

- SQL injection on JPA EntityManager.createNativeQuery\(\) is not checked [\#15](https://github.com/find-sec-bugs/find-sec-bugs/issues/15)
- Add scala.util.Random to PredictableRandomDetector [\#17](https://github.com/find-sec-bugs/find-sec-bugs/pull/17) ([HairyFotr](https://github.com/HairyFotr))

**Fixed bugs:**

- The BAD\_HEXA\_CONVERSION detector seems to have issues when UnconditionalValueDerefAnalysis is run later [\#12](https://github.com/find-sec-bugs/find-sec-bugs/issues/12)
- Parent POM referenced but not published to Maven Central [\#11](https://github.com/find-sec-bugs/find-sec-bugs/issues/11)

## [version-1.2.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.2.0) (2013-10-30)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.1.0...version-1.2.0)

**Fixed bugs:**

- Findbugs Security Plugin [\#5](https://github.com/find-sec-bugs/find-sec-bugs/issues/5)
- Clarify the test scope of test dependencies. [\#13](https://github.com/find-sec-bugs/find-sec-bugs/pull/13) ([dbaxa](https://github.com/dbaxa))

## [version-1.1.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.1.0) (2013-07-11)
[Full Changelog](https://github.com/find-sec-bugs/find-sec-bugs/compare/version-1.0.0...version-1.1.0)

**Implemented enhancements:**

- Various fixes for findbugs.xml, messages.xml and ECB detection [\#9](https://github.com/find-sec-bugs/find-sec-bugs/pull/9) ([samuelreed](https://github.com/samuelreed))

**Fixed bugs:**

- NullPointerException at BadHexadecimalConversionDetector.java:65 [\#3](https://github.com/find-sec-bugs/find-sec-bugs/issues/3)
- Bug fix for BadHexadecimalConversionDetector [\#4](https://github.com/find-sec-bugs/find-sec-bugs/pull/4) ([pcavezzan](https://github.com/pcavezzan))
- Removed duplicate entry of bug pattern SERVLET\_HEADER. [\#1](https://github.com/find-sec-bugs/find-sec-bugs/pull/1) ([uhafner](https://github.com/uhafner))

## [version-1.0.0](https://github.com/find-sec-bugs/find-sec-bugs/tree/version-1.0.0) (2012-10-20)


\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*