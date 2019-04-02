import groovy.io.FileType

class MetadataFileUtil {

    static String metaDataDir = "../findsecbugs-plugin/src/main/resources/metadata"

    //Contains the bug descriptions
    static InputStream messagesStreamEn = new FileInputStream(new File(metaDataDir, "messages.xml"))
    static InputStream messagesStreamJa = new FileInputStream(new File(metaDataDir, "messages_ja.xml"))



    static buildMapping(InputStream xmlStream) {
        def rootXml = new XmlParser().parse(new InputStreamReader(xmlStream, "UTF-8"))
        println "Loading the mapping for the messages"
        def bugsBinding = [:]

        bugsBinding['bugPatterns'] = []

        bugsBinding['nbPatterns'] = 0
        bugsBinding['nbDetectors'] = 0

        rootXml.BugPattern.each { pattern ->
            bugsBinding['bugPatterns'].add(
                    ['title'      : pattern.ShortDescription.text().replaceAll(" in \\{1\\}", ""),
                     'description': pattern.Details.text(),
                     'type'       : pattern.attribute("type")])
            //println pattern.ShortDescription.text()
            bugsBinding['nbPatterns']++
        }

        rootXml.Detector.each { detector ->
            bugsBinding['nbDetectors']++
        }
        return bugsBinding
    }

    static int countSignature(String folder) {
        def dir = new File(folder)

        int count = 0
        dir.eachFileRecurse(FileType.FILES) { file ->
            //println file.getName()
            file.eachLine { line ->
                String lineTrim = line.trim()
                if (lineTrim == "" || lineTrim.startsWith("-")) return
                count++
            }
        }
        return count
    }
}