# Find Security Bugs [![Build Status](https://secure.travis-ci.org/h3xstream/find-sec-bugs.png?branch=master)](http://travis-ci.org/h3xstream/find-sec-bugs) [![Coverage Status](https://coveralls.io/repos/h3xstream/find-sec-bugs/badge.png?branch=master)](https://coveralls.io/r/h3xstream/find-sec-bugs?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.h3xstream.findsecbugs/findsecbugs-plugin/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.h3xstream.findsecbugs%22%20a%3A%22findsecbugs-plugin%22)

Find Security Bugs is the [FindBugs](http://findbugs.sourceforge.net/) plugin for security audits of Java web applications.

Website : http://h3xstream.github.io/find-sec-bugs/

## Notable contributions

 - [David Formánek](https://github.com/formanek) : 
   - Major improvements and refactoring on the taint analysis for injections. 
   - The creation of a detector for Hard coded passwords and cryptographic keys.
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

### Sonar Qube

![SonarQube](http://find-sec-bugs.github.io/images/screens/sonar.png)

## License

This software is release under [LGPL](http://www.gnu.org/licenses/lgpl.html).