import groovy.text.SimpleTemplateEngine

//For testing only (stub project.basedir input)
project = {}
project.basedir = new File(System.getProperty("user.dir"))

println project.basedir
def outputDirectory = new File(project.basedir , "/target")
println outputDirectory

def configDir = new File(project.basedir , '/src/main/resources/metadata')
def templateDir = new File(project.basedir , '/src/main/script')



f = new File(configDir, "messages.xml")

println "Importing message from " + f.getCanonicalPath()
rootXml = new XmlParser().parse(f)

def binding = [:]
binding['bugPatterns'] = []

rootXml.BugPattern.each { pattern ->
    binding['bugPatterns'].add(
            ['title': pattern.ShortDescription.text(),
                    'description': pattern.Details.text()])
    println pattern.ShortDescription.text()
}

template = new File(templateDir, "layout.htm")
generatedPage = new File(outputDirectory, "index.htm")

//Building template
def engine = new SimpleTemplateEngine()
result = engine.createTemplate(template).make(binding)

generatedPage.withWriter{
    w ->
    w << result.toString()
}