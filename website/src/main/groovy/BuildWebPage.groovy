import groovy.text.SimpleTemplateEngine

//Contains the bug descriptions
InputStream messagesStream = getClass().getResourceAsStream("/metadata/messages.xml")

//Html Template of the documentation page
def getTemplateReader(String path) {
    return new InputStreamReader(getClass().getResourceAsStream(path))
}

//Generated page will be place there
String outDir = binding.variables.containsKey("project") ? project.build.outputDirectory : System.getProperty("java.io.tmpdir")

outDir = "C:\\Code\\find-sec-bugs\\website\\src\\main\\resources\\www"
def outputFile(outDir,file) {
    return new File(outDir, file)
}

//Loading detectors

println "Importing message from messages.xml"
rootXml = new XmlParser().parse(messagesStream)

println "Mapping the descriptions to the templates"
def bugsBinding = [:]
bugsBinding['bugPatterns'] = []

bugsBinding['nbPatterns'] = 0
bugsBinding['nbDetectors'] = 0

rootXml.BugPattern.each { pattern ->
    bugsBinding['bugPatterns'].add(
            ['title': pattern.ShortDescription.text(),
             'description': pattern.Details.text(),
             'type':pattern.attribute("type")])
    println pattern.ShortDescription.text()
    bugsBinding['nbPatterns']++
}

rootXml.Detector.each { detector ->
    bugsBinding['nbDetectors']++
}

//Version and download links

downloadUrl = "http://search.maven.org/remotecontent?filepath=com/h3xstream/findsecbugs/findsecbugs-plugin/1.3.0/findsecbugs-plugin-1.3.0.jar"
mavenCentralSearch = "http://search.maven.org/#search|gav|1|g:%22com.h3xstream.findsecbugs%22 AND a:%22findsecbugs-plugin%22"
latestVersion = "1.3.0"

//Screenshots

screenshots = []
screenshots.add(['title':'Eclipse',
                 'description':'<a href="http://marketplace.eclipse.org/content/findbugs-eclipse-plugin">Eclipse plugin</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/eclipse.png'])
screenshots.add(['title':'IntelliJ / Android Studio',
                 'description':'<a href="https://plugins.jetbrains.com/plugin/3847?pr=idea">IntelliJ plugin</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/intellij.png'])
screenshots.add(['title':'Sonar Qube',
                 'description':'<a href="http://docs.sonarqube.org/display/SONAR/Findbugs+Plugin">Sonar Qube</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/sonar.png'])

//Generate

def engine = new SimpleTemplateEngine()

println "Writing the template to ${outDir}/index.htm"



outputFile(outDir,"index.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Home'])
        w << engine.createTemplate(getTemplateReader("/home.htm")).make(['latestVersion':latestVersion,
                                                                         'nbPatterns':bugsBinding['nbPatterns'],
                                                                         'screenshots':screenshots])
        w << engine.createTemplate(getTemplateReader("/social.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

outputFile(outDir,"download.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(
                ['title':'Download'])
        w << engine.createTemplate(getTemplateReader("/download.htm")).make(
                ['downloadUrl':downloadUrl,'latestVersion':latestVersion,'mavenCentralSearch':mavenCentralSearch])
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

outputFile(outDir,"tutorials.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Tutorials'])
        w << engine.createTemplate(getTemplateReader("/tutorials.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

outputFile(outDir,"security.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Getting Started in Security'])
        w << engine.createTemplate(getTemplateReader("/security.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

outputFile(outDir,"bugs.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Bug Patterns'])
        w << engine.createTemplate(getTemplateReader("/bugs.htm")).make(bugsBinding)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}


outputFile(outDir,"license.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'License'])
        w << engine.createTemplate(getTemplateReader("/license.htm")).make(bugsBinding)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}