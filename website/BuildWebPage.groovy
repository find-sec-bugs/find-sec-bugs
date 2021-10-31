import groovy.text.SimpleTemplateEngine

import static MetadataFileUtil.*;

System.setProperty("file.encoding", "UTF-8")

//Html Template of the documentation page
def getTemplateReader(String path) {
    return new InputStreamReader(
            new FileInputStream(new File("templates", path)), "UTF-8");
}

//Generated page will be place there
outDir = "out_web/"

//Loading detectors
println "Importing message from messages.xml"
def bugsBindingEn = buildMapping(messagesStreamEn)
bugsBindingEn['lang'] = 'en'
def bugsBindingJa = buildMapping(messagesStreamJa)
bugsBindingJa['lang'] = 'ja'

nbSignatures = countSignature("../findsecbugs-plugin/src/main/resources/injection-sinks/") + countSignature("../findsecbugs-plugin/src/main/resources/password-methods/")

//Version and download links


latestVersion = "1.11.0"
downloadUrl = "https://search.maven.org/remotecontent?filepath=com/h3xstream/findsecbugs/findsecbugs-plugin/${latestVersion}/findsecbugs-plugin-${latestVersion}.jar"
mavenCentralSearch = "https://search.maven.org/#search|gav|1|g:%22com.h3xstream.findsecbugs%22 AND a:%22findsecbugs-plugin%22"
releaseNotesUrl = "https://github.com/find-sec-bugs/find-sec-bugs/releases/latest" //This link redirect to the latest release
latestUpdateDate = "October 29th, 2020"

//Screenshots

screenshots = []
screenshots.add(['title':'Eclipse',
                 'description':'<a target="_blank" href="http://marketplace.eclipse.org/content/findbugs-eclipse-plugin">Eclipse plugin</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/eclipse.png'])
screenshots.add(['title':'IntelliJ / Android Studio',
                 'description':'<a target="_blank" href="https://plugins.jetbrains.com/plugin/3847?pr=idea">IntelliJ plugin</a> with FindSecurityBugs detectors.',
                 'path':'images/screens/intellij.png'])
screenshots.add(['title':'Sonar Qube',
                 'description':'<a target="_blank" href="http://docs.sonarqube.org/display/SONAR/Findbugs+Plugin">Sonar Qube</a> with FindBugs plugin (version 3.2+).',
                 'path':'images/screens/sonar.png'])

//Generate

def engine = new SimpleTemplateEngine()

println "Writing the template to ${outDir}/index.htm"



new File(outDir,"index.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['lang':'en','title':'Home','section':'home','pageRedirect':''])
        w << engine.createTemplate(getTemplateReader("/home.htm")).make(['latestVersion':latestVersion,
                                                                         'latestUpdateDate':latestUpdateDate,
                                                                         'nbPatterns':bugsBindingEn['nbPatterns'],
                                                                         'nbSignatures':nbSignatures,
                                                                         'screenshots':screenshots,
                                                                         'releaseNotesUrl':releaseNotesUrl
                                                                         ])
        w << engine.createTemplate(getTemplateReader("/social.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"download.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(
                ['lang':'en','title':'Download','section':'download','pageRedirect':'download.htm'])
        w << engine.createTemplate(getTemplateReader("/download.htm")).make(
                ['downloadUrl':downloadUrl,'latestVersion':latestVersion,'mavenCentralSearch':mavenCentralSearch])
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"tutorials.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['lang':'en','title':'Tutorials','section':'tutorials','pageRedirect':'tutorials.htm'])
        w << engine.createTemplate(getTemplateReader("/tutorials.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"security.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['lang':'en','title':'Getting Started in Security','section':'tutorials','pageRedirect':'security.htm'])
        w << engine.createTemplate(getTemplateReader("/security.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"bugs.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['lang':'en','title':'Bug Patterns','section':'bugs','pageRedirect':'bugs.htm'])
        w << engine.createTemplate(getTemplateReader("/bugs.htm")).make(bugsBindingEn)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}

new File(outDir,"bugs_ja.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['lang':'ja','title':'Bug Patterns','section':'bugs','pageRedirect':'bugs_ja.htm'])
        w << engine.createTemplate(getTemplateReader("/bugs.htm")).make(bugsBindingJa)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}


new File(outDir,"license.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['lang':'en','title':'License','section':'license','pageRedirect':'license.htm'])
        w << engine.createTemplate(getTemplateReader("/license.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make(['latestVersion':latestVersion])
}
