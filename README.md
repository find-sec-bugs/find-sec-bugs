## Resources

Website : http://h3xstream.github.com/find-sec-bugs/

Build status : [![Build Status](https://secure.travis-ci.org/h3xstream/find-sec-bugs.png?branch=master)](http://travis-ci.org/h3xstream/find-sec-bugs)

Code coverage : [![Coverage Status](https://coveralls.io/repos/h3xstream/find-sec-bugs/badge.png?branch=master)](https://coveralls.io/r/h3xstream/find-sec-bugs?branch=master)

## License

This software is release under [LGPL](http://www.gnu.org/licenses/lgpl.html).

## Milestones (Planned features)

Aside from adding various new detectors, there are few features that are planned.

### Version 1.0

- Introduce a basic set of detectors
- Create a [tutorial for Eclipse IDE](https://github.com/h3xstream/find-sec-bugs/wiki/Eclipse-tutorial)

### Version 1.1

- Create a [tutorial for Jenkins](https://github.com/h3xstream/find-sec-bugs/wiki/Jenkins-integration)
- ~~Create a maven repository host on GitHub~~ Publish the plugin on [Maven central repository](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.h3xstream.findsecbugs%22)

### Version 1.2 (current)

- Create a [detector for XSS in JSP](http://h3xstream.github.io/find-sec-bugs/bugs.htm#XSS_JSP_PRINT)
- Performance improvements (mainly injections' detectors)
- Initiate a set of detectors for Groovy (Grails, Ratpack) and Scala (Play2, Scalatra)
- Improve the flow analysis of variables (for string construction)
