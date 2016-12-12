# Find Security Bugs [![Build Status](https://secure.travis-ci.org/find-sec-bugs/find-sec-bugs.png?branch=master)](http://travis-ci.org/find-sec-bugs/find-sec-bugs) [![Coverage Status](https://coveralls.io/repos/find-sec-bugs/find-sec-bugs/badge.png?branch=master)](https://coveralls.io/r/find-sec-bugs/find-sec-bugs?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.h3xstream.findsecbugs/findsecbugs-plugin/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.h3xstream.findsecbugs%22%20a%3A%22findsecbugs-plugin%22) [![Dependency Status](https://www.versioneye.com/user/projects/5751d1e07757a00041b3a1dd/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5751d1e07757a00041b3a1dd)

Find Security Bugs is the [FindBugs](http://findbugs.sourceforge.net/) plugin for security audits of Java web applications.

Website : http://find-sec-bugs.github.io/

## Main developers

 - [Philippe Arteau](https://github.com/h3xstream)
 - [David Formánek](https://github.com/formanek)

## Notable contributions

 - [David Formánek](https://github.com/formanek) : 
   - Major improvements and refactoring on the taint analysis for injections.
   - The creation of a detector for Hard coded passwords and cryptographic keys.
 - [Maxime Nadeau](https://github.com/MaxNad)
   - New detectors surrounding the Play Framework and improvements related to Scala.
 - [Naoki Kimura](https://github.com/naokikimura) : 
   - Detector for [injection in custom API](http://h3xstream.github.io/find-sec-bugs/bugs.htm#CUSTOM_INJECTION)
   - Translation of [messages in Japanese](http://h3xstream.github.io/find-sec-bugs/bugs_ja.htm)
 - [Dave Wichers](https://github.com/davewichers) :
   - Improvement to vulnerability descriptions

## Screenshots

### Eclipse

![Eclipse](http://find-sec-bugs.github.io/images/screens/eclipse.png)

### IntelliJ / Android Studio

![IntelliJ](http://find-sec-bugs.github.io/images/screens/intellij.png)

### SonarQube

![SonarQube](http://find-sec-bugs.github.io/images/screens/sonar.png)

### Codacy

![Codacy](http://find-sec-bugs.github.io/images/screens/codacy.png)

## License

This software is release under [LGPL](http://www.gnu.org/licenses/lgpl.html).
