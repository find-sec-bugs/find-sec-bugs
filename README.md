# OWASP Find Security Bugs 
[![Java CI with SpotBugs](https://github.com/find-sec-bugs/find-sec-bugs/actions/workflows/spotbugs.yml/badge.svg)](https://github.com/find-sec-bugs/find-sec-bugs/actions/workflows/spotbugs.yml) [![codecov](https://codecov.io/gh/find-sec-bugs/find-sec-bugs/branch/master/graph/badge.svg)](https://codecov.io/gh/find-sec-bugs/find-sec-bugs) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.h3xstream.findsecbugs/findsecbugs-plugin/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.h3xstream.findsecbugs%22%20a%3A%22findsecbugs-plugin%22) [![Slack Channel](https://img.shields.io/badge/slack-OWASP%2ffind--sec--bugs-orange?logo=slack)](https://app.slack.com/client/T04T40NHX/CN8G79Y6P)


Find Security Bugs is the [SpotBugs](https://spotbugs.github.io/) plugin for security audits of Java web applications.

Website : http://find-sec-bugs.github.io/

## Main developers

 - [Philippe Arteau](https://github.com/h3xstream) from [GoSecure](https://github.com/gosecure)
 - [David Formánek](https://github.com/formanek)
 - [Tomáš Polešovský](https://github.com/topolik) from [Liferay](https://github.com/liferay)

## Notable contributions

 - [David Formánek](https://github.com/formanek)
   - Major improvements and refactoring on the taint analysis for injections.
   - The creation of a detector for hard coded passwords and cryptographic keys.
 - [Tomáš Polešovský](https://github.com/topolik)
   - Improvements and bug fixes related to the taint analysis.
 - [Maxime Nadeau](https://github.com/MaxNad)
   - New detectors surrounding the Play Framework and improvements related to Scala.
 - [Naoki Kimura](https://github.com/naokikimura)
   - Detector for [injection in custom API](http://h3xstream.github.io/find-sec-bugs/bugs.htm#CUSTOM_INJECTION)
   - Translation of [messages in Japanese](http://h3xstream.github.io/find-sec-bugs/bugs_ja.htm)
 - [Dave Wichers](https://github.com/davewichers)
   - Improvement to vulnerability descriptions

## Project Sponsors

The development of Find Security Bugs is supported by [GoSecure](https://github.com/gosecure) since 2016. The support includes the development of new detectors and the research for new vulnerability classes.

![GoSecure Logo](website/out_web/images/gosecure.png)

## Screenshots

### Eclipse

![Eclipse](https://find-sec-bugs.github.io/images/screens/eclipse.png)

### IntelliJ / Android Studio

![IntelliJ](https://find-sec-bugs.github.io/images/screens/intellij.png)

### SonarQube

![SonarQube](https://find-sec-bugs.github.io/images/screens/sonar.png)

## License

This software is release under [LGPL](http://www.gnu.org/licenses/lgpl.html).
