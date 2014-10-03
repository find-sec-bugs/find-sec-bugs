import groovy.text.SimpleTemplateEngine

//Contains the bug descriptions
InputStream messagesStream = getClass().getResourceAsStream("/metadata/messages.xml")

//Html Template of the documentation page
def getTemplateReader(String path) {
    return new InputStreamReader(getClass().getResourceAsStream(path))
}

//Generated page will be place there
String outDir = binding.variables.containsKey("project") ? project.build.outputDirectory : System.getProperty("java.io.tmpdir")

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

downloadUrl = "http://search.maven.org/remotecontent?filepath=com/h3xstream/findsecbugs/findsecbugs-plugin/1.2.1/findsecbugs-plugin-1.2.1.jar"
mavenCentralSearch = "http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.h3xstream.findsecbugs%22%20a%3A%22findsecbugs-plugin%22"
latestVersion = "v 1.2.1"

//Generate

def engine = new SimpleTemplateEngine()

println "Writing the template to ${outDir}/index.htm"



outputFile(outDir,"index.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Home'])
        w << engine.createTemplate(getTemplateReader("/home.htm")).make(['latestVersion':latestVersion])
        w << engine.createTemplate(getTemplateReader("/social.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make()
}

outputFile(outDir,"download.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(
                ['title':'Download'])
        w << engine.createTemplate(getTemplateReader("/download.htm")).make(
                ['downloadUrl':downloadUrl,'latestVersion':latestVersion,'mavenCentralSearch':mavenCentralSearch])
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make()
}

outputFile(outDir,"tutorials.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Tutorials'])
        w << engine.createTemplate(getTemplateReader("/tutorials.htm")).make()
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make()
}

outputFile(outDir,"bugs.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'Bugs Description'])
        w << engine.createTemplate(getTemplateReader("/bugs.htm")).make(bugsBinding)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make()
}


outputFile(outDir,"license.htm").withWriter {
    w ->
        w << engine.createTemplate(getTemplateReader("/common_header.htm")).make(['title':'License'])
        w << engine.createTemplate(getTemplateReader("/license.htm")).make(bugsBinding)
        w << engine.createTemplate(getTemplateReader("/common_footer.htm")).make()
}