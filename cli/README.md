# Find Security Bugs - Command Line Version

The latest package version is made available in the release section : https://github.com/find-sec-bugs/find-sec-bugs/releases

## Building the package

1. Update the version of the latest release of Find Security Bugs and SpotBugs in `gradle.properties`

2. Run the following command

```
gradle packageCli
```

This should produce a zip archive with a portable version of Find Security Bugs (`findsecbugs-cli-*.zip`).


The spotbugs dependencies and the latest FindSecurityBugs plugin will be place in the lib directory.
The lib directory need to be clear manually if the versions are changed from the gradle build file.
