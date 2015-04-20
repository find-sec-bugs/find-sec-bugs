# Change Log

## [version-1.4.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.4.0) (2015-04-03)

[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.3.1...version-1.4.0)

**Implemented enhancements:**

- Upgrade to findbugs 3.0.0 or higher. [\#37](https://github.com/h3xstream/find-sec-bugs/issues/37)

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

- Externalize Injection sources [\#28](https://github.com/h3xstream/find-sec-bugs/pull/28) ([naokikimura](https://github.com/naokikimura))

## [version-1.3.0](https://github.com/h3xstream/find-sec-bugs/tree/version-1.3.0) (2015-01-02)

[Full Changelog](https://github.com/h3xstream/find-sec-bugs/compare/version-1.2.1...version-1.3.0)

**Implemented enhancements:**

- XXE - Separate guidelines \(XMLReader/SaxParser/DocumentParser\) [\#27](https://github.com/h3xstream/find-sec-bugs/issues/27)

- XXE - Avoid false positive when secure features are set. [\#26](https://github.com/h3xstream/find-sec-bugs/issues/26)

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

