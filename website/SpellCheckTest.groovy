
@Grapes([
        @Grab(group='org.languagetool', module='language-en', version='4.4'),
        @Grab(group='org.jsoup', module='jsoup', version='1.11.3')
])
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.spelling.SpellingCheckRule
import org.jsoup.Jsoup;

import static MetadataFileUtil.*;


bugsBindingEn = buildMapping(messagesStreamEn)


JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());


def isCamelCase(String word) {
    i=0
    nbUpper=0
    nbLower=0
    nbSpace=0

    firstUpper=false

    word.toCharArray().each { Character ch ->
        if(i==0 && Character.isUpperCase(ch)) {
            firstUpper=true
        }

        if(Character.isUpperCase(ch)) {
            nbUpper++
        }
        else {
            nbLower++
        }

        if(ch == ' ') { //Just in case it is more than one word
            nbSpace++
        }
        i++
    }

    //println("$word : Length ${word.length()} NbUpper $nbUpper")
    return firstUpper && ((nbUpper > 1 && nbLower > 1) || (word.length() == nbUpper)) && nbSpace == 0
}

def spellCheckText(text,langTool) {
    List matches = langTool.check(text);

    matches.each { match ->

        bogusSegment = text.substring(match.getFromPos(), match.getToPos())

        if(isCamelCase(bogusSegment)) { //This is a class name
            //println(" [*] $bogusSegment is OK")
            return
        }

        if(match.getMessage().startsWith("Use a smart opening quote here")) {
            return;
        }
        if(match.getMessage().startsWith("Use a smart closing quote here")) {
            return;
        }
        if(match.getMessage().startsWith("This sentence does not start with an uppercase letter")) {
            return;
        }


        println("=========== /!\\ =============")
        println(text)
        if(text.length() > match.getToPos()+2) {
            nextTwo = text.substring(match.getToPos()+1,match.getToPos()+2)
        }

        i = 0
        highlighting = false
        while(i < match.getToPos()) {
            if(i == match.getFromPos()) {
                highlighting=true
            }

            print(highlighting ? "^":" ")

            i++
        }
        println()

        println("** ${match.getMessage()} (at characters ${match.getFromPos()}-${match.getToPos()})");
        println("** Suggested correction(s): " +
                match.getSuggestedReplacements());
        println()
    }
    return matches.size();
}

langTool.getAllActiveRules().each { rule ->
    if (rule instanceof SpellingCheckRule) {
        def sellCheckRule = (SpellingCheckRule)rule
        new File("./spell/dictionary.txt").eachLine{ String it, i ->
            if(it != "") sellCheckRule.addIgnoreTokens(Arrays.asList(it.trim()));
        }
    }
}


totalErrors = 0
bugsBindingEn['bugPatterns'].each { bug ->
    //totalErrors += spellCheckText(bug.title, langTool)

    description_stripped = bug.description
    //description_stripped = description_stripped.replaceAll(~/\[\.\.\.\]/, "") //Domains

    description_stripped = description_stripped.replaceAll(" - ", " ") //Common separator in the references section
    description_stripped = description_stripped.replaceAll(~/\\[\\.\\.\\.\\]/, "")
    description_stripped = description_stripped.replaceAll(~/e\\.g\\.([\\s,])?/, "")
    description_stripped = description_stripped.replaceAll(~/i\\.e\\.([\\s,])?/,"")
    description_stripped = description_stripped.replaceAll(~/(http:\/\/)?[a-zA-Z\.]+\\.com/, "URL") //Domains
    description_stripped = description_stripped.replaceAll(~/(http:\/\/)?[a-zA-Z\.]+\\.org/, "URL") //Domains
    description_stripped = description_stripped.replaceAll(~/(http:\/\/)?[a-zA-Z\.]+\\.net/, "URL") //Domains
    description_stripped = description_stripped.replaceAll(~/(http:\/\/)?[a-zA-Z\.]+\\.au/, "URL") //Domains
    description_stripped = description_stripped.replaceAll(~/<%[^<]+%>/, "code") //JSTL
    description_stripped = description_stripped.replaceAll(~/<code>[^<]+<\/code>/, "code") //Code inline
    description_stripped = description_stripped.replaceAll(~/<pre>[^<]+<\/pre>/, "") //Code section
    description_stripped = Jsoup.parse(description_stripped).text()
    //println(description_stripped)



    sentences = description_stripped.split("\\.")


    sentences.each { s->
        totalErrors += spellCheckText(s, langTool)
    }
    //

}

if(totalErrors==0) {
    println("No error. Congrats!")
}