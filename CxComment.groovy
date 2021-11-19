@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
import groovyx.net.http.HTTPBuilder

def branch = request.getBranch()
def env = System.getenv()
def commitId = env['GITHUB_SHA']
env.each{
println it
} 
String scanComment = "Repo: aaa | Branch: $branch | Commit ID: $commitId"

println "INFO : Scanning code from $scanComment"

return scanComment
