//@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
//import groovyx.net.http.HTTPBuilder
def branch = request.getBranch()
def hash = request.getHash()
def repoUrl = request.getRepoUrl()
String comment = "$branch | $hash"
println "" + request.toString()
println "INFO : Repo URL: $repoUrl | Branch: $branch | Commit Hash: $hash"
return comment
