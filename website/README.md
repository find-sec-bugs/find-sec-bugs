This folder contains scripts to automate the generation of static files based on the messages.
This way the messages are visible on IDE but also available on the web on a publicly visible page.

```
> groovy BuildWebPage.groovy
[...]
Writing the template to out_web//index.htm
```

```
> groovy BuildSonarXmlFiles.groovy
Building ruleset findbugs (C:\Code\workspace-java\find-sec-bugs\website\out_sonar\rules-findbugs.xml)
Building ruleset jsp (C:\Code\workspace-java\find-sec-bugs\website\out_sonar\rules-jsp.xml)
Building profile findbugs-security-audit (C:\Code\workspace-java\find-sec-bugs\website\out_sonar\profiles-findbugs-security-audit.xml)
Building profile findbugs-security-minimal (C:\Code\workspace-java\find-sec-bugs\website\out_sonar\profiles-findbugs-security-minimal.xml)
Building profile findbugs-security-jsp (C:\Code\workspace-java\find-sec-bugs\website\out_sonar\profiles-findbugs-security-jsp.xml)
```