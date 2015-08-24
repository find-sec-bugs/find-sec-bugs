# Change Log

## [version-1.4.2](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.2) (2015-08-18)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.4.1...version-1.4.2)

**Implemented enhancements:**

- SQL Injection detected when StringBuilder is used with constant strings [\#14](https://github.com/h3xstream/find-sec-bugs/issues/14)

**Fixed bugs:**

- Remove slash from XXE short message [\#68](https://github.com/h3xstream/find-sec-bugs/issues/68)

**Merged pull requests:**

- Refactoring of classes for taint analysis [\#71](https://github.com/h3xstream/find-sec-bugs/pull/71) ([formanek](https://github.com/formanek))
- Translate a message of HARD\_CODE\_KEY pattern. [\#70](https://github.com/h3xstream/find-sec-bugs/pull/70) ([naokikimura](https://github.com/naokikimura))
- Taint sources locations added to bug reports [\#69](https://github.com/h3xstream/find-sec-bugs/pull/69) ([formanek](https://github.com/formanek))
- Separated hard coded password and key reporting [\#67](https://github.com/h3xstream/find-sec-bugs/pull/67) ([formanek](https://github.com/formanek))
- Taint sources and improved taint transfer [\#66](https://github.com/h3xstream/find-sec-bugs/pull/66) ([formanek](https://github.com/formanek))
- Improved hardcoded passwords and key detector + taint analysis [\#63](https://github.com/h3xstream/find-sec-bugs/pull/63) ([formanek](https://github.com/formanek))
- Allow analyze to set classpath entries [\#60](https://github.com/h3xstream/find-sec-bugs/pull/60) ([mbmihura](https://github.com/mbmihura))
- website: corrected typos [\#59](https://github.com/h3xstream/find-sec-bugs/pull/59) ([obilodeau](https://github.com/obilodeau))

## [version-1.4.1](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.1) (2015-05-30)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.4.0...version-1.4.1)

**Implemented enhancements:**

- Create a tutorial for IntelliJ IDE [\#51](https://github.com/h3xstream/find-sec-bugs/issues/51)
- Detector hard coded Spring OAuth secret key [\#57](https://github.com/h3xstream/find-sec-bugs/issues/57)
- Add CWE references to messages \(few missing\) [\#52](https://github.com/h3xstream/find-sec-bugs/issues/52)
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

**Merged pull requests:**

- Create messages\_ja.xml [\#38](https://github.com/h3xstream/find-sec-bugs/pull/38) ([naokikimura](https://github.com/naokikimura))
- Enable additional signatures to detector of injection [\#36](https://github.com/h3xstream/find-sec-bugs/pull/36) ([naokikimura](https://github.com/naokikimura))

## [version-1.3.1](https://github.com/h3xstream/find-sec-bugs/tree/version-1.3.1) (2015-02-23)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.3.0...version-1.3.1)

**Implemented enhancements:**

- Add supports for the new URL specification for bug reference [\#35](https://github.com/h3xstream/find-sec-bugs/issues/35)
- Higher priority for injections [\#32](https://github.com/h3xstream/find-sec-bugs/issues/32)
- Remove ESAPI references in messages [\#31](https://github.com/h3xstream/find-sec-bugs/issues/31)

**Fixed bugs:**

- MethodUnprofitableException throwing could be suppressed [\#29](https://github.com/h3xstream/find-sec-bugs/issues/29)
- CipherWithNoIntegrityDetector throws exception on algorithm-only cipher lookups [\#24](https://github.com/h3xstream/find-sec-bugs/issues/24)

**Merged pull requests:**

- Copy all files in metadata folder [\#30](https://github.com/h3xstream/find-sec-bugs/pull/30) ([jsotuyod](https://github.com/jsotuyod))

## [version-1.3.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.3.0) (2015-01-02)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.2.1...version-1.3.0)

**Implemented enhancements:**

- XXE - Separate guidelines \(XMLReader/SaxParser/DocumentParser\) [\#27](https://github.com/h3xstream/find-sec-bugs/issues/27)
- XXE - Avoid false positive when secure features are set. [\#26](https://github.com/h3xstream/find-sec-bugs/issues/26)
- Fix links in the descriptions [\#25](https://github.com/h3xstream/find-sec-bugs/issues/25)
- JDO Query - Potential Injections [\#23](https://github.com/h3xstream/find-sec-bugs/issues/23)
- JDO PersistenceManager - Potential Injections [\#22](https://github.com/h3xstream/find-sec-bugs/issues/22)
- Hibernate Restrictions API - Potential Injections [\#21](https://github.com/h3xstream/find-sec-bugs/issues/21)
- Tag 1.2.1 release [\#18](https://github.com/h3xstream/find-sec-bugs/issues/18)

**Fixed bugs:**

- Fix links in the descriptions [\#25](https://github.com/h3xstream/find-sec-bugs/issues/25)

## [version-1.2.1](https://github.com/h3xstream/find-sec-bugs/tree/version-1.2.1) (2014-10-03)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.2.0...version-1.2.1)

**Implemented enhancements:**

- SQL injection on JPA EntityManager.createNativeQuery\(\) is not checked [\#15](https://github.com/h3xstream/find-sec-bugs/issues/15)

**Fixed bugs:**

- The BAD\_HEXA\_CONVERSION detector seems to have issues when UnconditionalValueDerefAnalysis is run later [\#12](https://github.com/h3xstream/find-sec-bugs/issues/12)
- Parent POM referenced but not published to Maven Central [\#11](https://github.com/h3xstream/find-sec-bugs/issues/11)

**Merged pull requests:**

- Add scala.util.Random to PredictableRandomDetector [\#17](https://github.com/h3xstream/find-sec-bugs/pull/17) ([HairyFotr](https://github.com/HairyFotr))

## [version-1.2.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.2.0) (2013-10-30)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.1.0...version-1.2.0)

**Fixed bugs:**

- Findbugs Security Plugin [\#5](https://github.com/h3xstream/find-sec-bugs/issues/5)

**Merged pull requests:**

- Clarify the test scope of test dependencies. [\#13](https://github.com/h3xstream/find-sec-bugs/pull/13) ([dbaxa](https://github.com/dbaxa))

## [version-1.1.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.1.0) (2013-07-11)
[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.0.0...version-1.1.0)

**Fixed bugs:**

- NullPointerException at BadHexadecimalConversionDetector.java:65 [\#3](https://github.com/h3xstream/find-sec-bugs/issues/3)

**Merged pull requests:**

- Various fixes for findbugs.xml, messages.xml and ECB detection [\#9](https://github.com/h3xstream/find-sec-bugs/pull/9) ([samuelreed](https://github.com/samuelreed))
- Bug fix for BadHexadecimalConversionDetector [\#4](https://github.com/h3xstream/find-sec-bugs/pull/4) ([pcavezzan](https://github.com/pcavezzan))
- Removed duplicate entry of bug pattern SERVLET\_HEADER. [\#1](https://github.com/h3xstream/find-sec-bugs/pull/1) ([uhafner](https://github.com/uhafner))

## [version-1.0.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.0.0) (2012-10-20)


\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*