<!--
Thank you for reporting an issue.

If you have a question regarding existing features or if you need support,
please ask the question on StackOverflow :
Create a issue using the tag "find-sec-bugs" :
https://stackoverflow.com/questions/ask?tags=find-sec-bugs,spotbugs,java

GitHub issues are mainly used for tracking enhancements and bugs.

--><!-- Enter your issue details below this comment. -->

## Environment

<!-- The versions used: Gradle 4.5/4.6, Maven 3.5.X, Java 7/8/9, SpotBugs 3.1.6/..., FindSecBugs 1.8.0/.. -->

| Component          | Version |
| ------------------ | ------- |
| Maven              | ?????   |
| Gradle             | ?????   |
| Java               | ?????   |
| SpotBugs           | ?????   |
| FindSecBugs        | ?????   |

## Problem

<!-- Include the elements that applied:
     - Description of the expected behavior and the actual result.
     - Stacktrace
     - Maven/Gradle/Ant output
-->

## Code (If needed)

<!-- Include the Java code samples or ZIP files of a sample project that reproduce the given bug. -->

```java
public class BugSample1 {
  public static void hello(String message) {
       
    //Something
    Runnable r = () -> System.out.println(message);
   
    r.run();
  }
}
```
