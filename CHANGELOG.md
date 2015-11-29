# Change Log

## [version-1.4.4](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.4) (2015-11-20)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.4.3...version-1.4.4)

**Implemented enhancements:**

- Path traversal and Xpath injection detectors should use taint analysis [\#97](https://github.com/h3xstream/find-sec-bugs/issues/97)
- Detector for external control of configuration \(CWE-15\) [\#124](https://github.com/h3xstream/find-sec-bugs/issues/124)
- Detector for CRLF injection in logs \(CWE-117\) [\#123](https://github.com/h3xstream/find-sec-bugs/issues/123)
- Detector for HTTP response splitting [\#121](https://github.com/h3xstream/find-sec-bugs/issues/121)
- Improvements for JSP support [\#110](https://github.com/h3xstream/find-sec-bugs/issues/110)
- Missing taint sinks for LDAP Injection [\#105](https://github.com/h3xstream/find-sec-bugs/issues/105)
- New rule : Detect dynamic JSP Includes [\#104](https://github.com/h3xstream/find-sec-bugs/issues/104)
- Standalone command line tool to scan jars with or without the source [\#100](https://github.com/h3xstream/find-sec-bugs/issues/100)
- Better support for collections [\#99](https://github.com/h3xstream/find-sec-bugs/issues/99)
- Consider inheritance for method summaries [\#98](https://github.com/h3xstream/find-sec-bugs/issues/98)
- Refactor injection detectors [\#96](https://github.com/h3xstream/find-sec-bugs/issues/96)
- New Rule : Detect Spring Eval JSP taglib [\#55](https://github.com/h3xstream/find-sec-bugs/issues/55)

**Fixed bugs:**

- Path traversal false positives [\#113](https://github.com/h3xstream/find-sec-bugs/issues/113)

**Closed issues:**

- mvn compile failing after adding findsecbugs-plugin [\#128](https://github.com/h3xstream/find-sec-bugs/issues/128)
- Add methods for weak message digest [\#120](https://github.com/h3xstream/find-sec-bugs/issues/120)
- How can I mark / exclude false positives? [\#116](https://github.com/h3xstream/find-sec-bugs/issues/116)
- New Rule : JSTL out escapeXml=false [\#114](https://github.com/h3xstream/find-sec-bugs/issues/114)
- Missing taint sinks for Spring SQL injection [\#109](https://github.com/h3xstream/find-sec-bugs/issues/109)
- Method arguments are not tainted if their derived summary is stored  [\#106](https://github.com/h3xstream/find-sec-bugs/issues/106)
- Push release 1.4.3 to upstream projects [\#101](https://github.com/h3xstream/find-sec-bugs/issues/101)

**Merged pull requests:**

- Add detector for java object deserialization [\#127](https://github.com/h3xstream/find-sec-bugs/pull/127) ([minlex](https://github.com/minlex))

## [version-1.4.3](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.3) (2015-09-16)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.4.2...version-1.4.3)

**Implemented enhancements:**

- All Runtime.exec methods should be taint sinks [\#92](https://github.com/h3xstream/find-sec-bugs/issues/92)
- Add coverage for LDAP injection [\#89](https://github.com/h3xstream/find-sec-bugs/issues/89)
- Improve the detection of weak message digest [\#88](https://github.com/h3xstream/find-sec-bugs/issues/88)
- Improve the detection in the use of old ciphers [\#87](https://github.com/h3xstream/find-sec-bugs/issues/87)
- Insecure cookie [\#86](https://github.com/h3xstream/find-sec-bugs/issues/86)
- Spring JDBC API [\#74](https://github.com/h3xstream/find-sec-bugs/issues/74)
- JDBC api coverage [\#73](https://github.com/h3xstream/find-sec-bugs/issues/73)
- False positive on Static IV when using Cipher.getIv\(\) [\#62](https://github.com/h3xstream/find-sec-bugs/issues/62)

**Fixed bugs:**

- Parametric taint state not changed when used as an argument of an unknown method [\#90](https://github.com/h3xstream/find-sec-bugs/issues/90)
- Bad method summaries derived for complex flow [\#85](https://github.com/h3xstream/find-sec-bugs/issues/85)
- Invalid taint modifications of local variables, when loaded from method summary [\#84](https://github.com/h3xstream/find-sec-bugs/issues/84)
- Taint not transfered in chained call of StringBuilder.append [\#83](https://github.com/h3xstream/find-sec-bugs/issues/83)
- Too many iterations bug [\#82](https://github.com/h3xstream/find-sec-bugs/issues/82)
- Issue with constructor with List and array as parameter \(Command injection detection\) [\#80](https://github.com/h3xstream/find-sec-bugs/issues/80)
- Fix DES detection [\#79](https://github.com/h3xstream/find-sec-bugs/issues/79)
- EntityManager createQuery trips SECSQLIJPA even with safe usage [\#76](https://github.com/h3xstream/find-sec-bugs/issues/76)
- The IV generation should only be verified for the encryption mode [\#64](https://github.com/h3xstream/find-sec-bugs/issues/64)

**Merged pull requests:**

- Improved taint analysis \(several bugs fixed, refactoring\) [\#91](https://github.com/h3xstream/find-sec-bugs/pull/91) ([formanek](https://github.com/formanek))

## [version-1.4.2](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.2) (2015-08-18)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.4.1...version-1.4.2)

**Implemented enhancements:**

- Improve taint analysis to avoid SQL Injection detected when StringBuilder is used [\#14](https://github.com/h3xstream/find-sec-bugs/issues/14)

**Fixed bugs:**

- Remove slash from XXE short message [\#68](https://github.com/h3xstream/find-sec-bugs/issues/68)

## [version-1.4.1](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.1) (2015-05-30)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.4.0...version-1.4.1)

**Implemented enhancements:**

- Detector hard coded Spring OAuth secret key [\#57](https://github.com/h3xstream/find-sec-bugs/issues/57)
- Add CWE references to messages \(few missing\) [\#52](https://github.com/h3xstream/find-sec-bugs/issues/52)
- Create a tutorial for IntelliJ IDE [\#51](https://github.com/h3xstream/find-sec-bugs/issues/51)
- Create a japanese page on the micro-website for the bug patterns [\#50](https://github.com/h3xstream/find-sec-bugs/issues/50)
- NetBeans tutorial [\#45](https://github.com/h3xstream/find-sec-bugs/issues/45)
- Update the documentation for Sonar Qube [\#44](https://github.com/h3xstream/find-sec-bugs/issues/44)

**Fixed bugs:**

- XXE - reader False Positive [\#47](https://github.com/h3xstream/find-sec-bugs/issues/47)
- Fix URLs in messages.xml [\#43](https://github.com/h3xstream/find-sec-bugs/issues/43)
- CustomInjectionSource.properties not found [\#42](https://github.com/h3xstream/find-sec-bugs/issues/42)

**Merged pull requests:**

- ECB and no integrity detection + tests [\#53](https://github.com/h3xstream/find-sec-bugs/pull/53) ([formanek](https://github.com/formanek))
- Update messages\_ja.xml [\#49](https://github.com/h3xstream/find-sec-bugs/pull/49) ([naokikimura](https://github.com/naokikimura))
- Detector for hard coded passwords and cryptographic keys [\#46](https://github.com/h3xstream/find-sec-bugs/pull/46) ([formanek](https://github.com/formanek))

## [version-1.4.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.0) (2015-04-03)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.3.1...version-1.4.0)

**Implemented enhancements:**

- Support java 8 - upgrade to findbugs 3.0.0 or higher. [\#37](https://github.com/h3xstream/find-sec-bugs/issues/37)
- New Android Security detectors [\#39](https://github.com/h3xstream/find-sec-bugs/issues/39)
- Move command injection to the main injection detector mecanism [\#33](https://github.com/h3xstream/find-sec-bugs/issues/33)

## [version-1.3.1](https://github.com/h3xstream/find-sec-bugs/tree/version-1.3.1) (2015-02-23)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.3.0...version-1.3.1)

**Implemented enhancements:**

- Add supports for the new URL specification for bug reference [\#35](https://github.com/h3xstream/find-sec-bugs/issues/35)
- Higher priority for injections [\#32](https://github.com/h3xstream/find-sec-bugs/issues/32)
- Remove ESAPI references in messages [\#31](https://github.com/h3xstream/find-sec-bugs/issues/31)
- XXE - Separate guidelines \(XMLReader/SaxParser/DocumentParser\) [\#27](https://github.com/h3xstream/find-sec-bugs/issues/27)
- XXE - Avoid false positive when secure features are set. [\#26](https://github.com/h3xstream/find-sec-bugs/issues/26)
- Fix links in the descriptions [\#25](https://github.com/h3xstream/find-sec-bugs/issues/25)
- JDO Query - Potential Injections [\#23](https://github.com/h3xstream/find-sec-bugs/issues/23)
- JDO PersistenceManager - Potential Injections [\#22](https://github.com/h3xstream/find-sec-bugs/issues/22)
- Hibernate Restrictions API - Potential Injections [\#21](https://github.com/h3xstream/find-sec-bugs/issues/21)

**Fixed bugs:**

- MethodUnprofitableException throwing could be suppressed [\#29](https://github.com/h3xstream/find-sec-bugs/issues/29)
- Fix links in the descriptions [\#25](https://github.com/h3xstream/find-sec-bugs/issues/25)
- CipherWithNoIntegrityDetector throws exception on algorithm-only cipher lookups [\#24](https://github.com/h3xstream/find-sec-bugs/issues/24)

## [version-1.3.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.3.0) (2015-01-02)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.2.1...version-1.3.0)

## [version-1.2.1](https://github.com/h3xstream/find-sec-bugs/tree/version-1.2.1) (2014-10-03)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.2.0...version-1.2.1)

**Fixed bugs:**

- Parent POM referenced but not published to Maven Central [\#11](https://github.com/h3xstream/find-sec-bugs/issues/11)

## [version-1.2.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.2.0) (2013-10-30)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.1.0...version-1.2.0)

**Fixed bugs:**

- Findbugs Security Plugin [\#5](https://github.com/h3xstream/find-sec-bugs/issues/5)

## [version-1.1.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.1.0) (2013-07-11)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.0.0...version-1.1.0)

## [version-1.0.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.0.0) (2012-10-20)


\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*