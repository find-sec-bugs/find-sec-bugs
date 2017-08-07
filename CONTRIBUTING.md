# How to contribute

## Submitting an issue

If you are submitting an issue, here are key points to keep in mind.

### Fatal Error

 - Include stack trace
 - Code sample to reproduce
 - Configuration (if appropriate)

### False positive

 - Provide Java/Scala/Groovy code to reproduce the issue
 - Explain why the vulnerability should not have been triggered

### New detector idea

 - Make sure it can be applied to more than one application
 - Provide: Vulnerable code, False positive and Solution

## Creating a Pull Request

 - The build is still passing `mvn clean install`
 - You have created test cases for new detector or signatures
 - Your pull request will be link in the release notes. Make sure the description is clear for the **users** 

## Find Security Bugs internals

If you want to learn more about Find Security Bugs internals, you read the wiki pages under the section [Developers corner](https://github.com/find-sec-bugs/find-sec-bugs/wiki#wrench-developers-corner).
