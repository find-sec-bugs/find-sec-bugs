import groovy.text.SimpleTemplateEngine

//Contains the bug descriptions
InputStream messagesStream = getClass().getResourceAsStream("/metadata/messages.xml")
//Html Template of the documentation page
Reader templateReader = new InputStreamReader(getClass().getResourceAsStream("/layout.htm"))
//Generated page will be place there
String outputDirectory = binding.variables.containsKey("project") ? project.build.outputDirectory : System.getProperty("java.io.tmpdir")

println "Importing message from messages.xml"
rootXml = new XmlParser().parse(messagesStream)

println "Mapping the descriptions to the templates"
def binding = [:]
binding['bugPatterns'] = []

rootXml.BugPattern.each { pattern ->
    binding['bugPatterns'].add(
            ['title': pattern.ShortDescription.text(),
                    'description': pattern.Details.text()])
    println pattern.ShortDescription.text()
}

generatedPage = new File(outputDirectory, "index.htm")

println "Writing the template to ${outputDirectory}/index.htm"
def engine = new SimpleTemplateEngine()
result = engine.createTemplate(templateReader).make(binding)

generatedPage.withWriter{
    w ->
    w << result.toString()
}