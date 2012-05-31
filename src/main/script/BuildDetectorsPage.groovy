import groovy.text.SimpleTemplateEngine

//For testing only (stub project.basedir input)
//project = {}
//project.basedir = new File(System.getProperty("user.dir"))

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

//About
binding['about'] = """
<b>Find Security Bugs</b> is plugin for FindBugs that aim to help security audit on Java web application.
"""

//Download

binding['download_url'] = "https://github.com/h3xstream/find-sec-bugs/downloads"

//Bugs
binding['bugs'] = """
The bug patterns identify by the plugin are not automatically a vulnerability or a defect.
It represent sensible points of the application that should be analyse closely.
"""

rootXml.BugPattern.each { pattern ->
    binding['bugPatterns'].add(
            ['title': pattern.ShortDescription.text(),
                    'description': pattern.Details.text()])
    println pattern.ShortDescription.text()
}

//License

binding['license'] = """
This software is release under <a href="http://www.gnu.org/licenses/lgpl.html">LGPL</a>.
"""

template = new File(templateDir, "layout.htm")
generatedPage = new File(outputDirectory, "index.htm")

//Building template
def engine = new SimpleTemplateEngine()
result = engine.createTemplate(template).make(binding)

generatedPage.withWriter{
    w ->
    w << result.toString()
}