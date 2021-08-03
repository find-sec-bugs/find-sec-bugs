---
name: Bug report
about: The scanner is not working as expected

---

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

## Code

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
