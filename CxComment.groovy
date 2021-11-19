@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )
import groovyx.net.http.HTTPBuilder

def branch = request.getBranch()
def env = System.getenv()
def commitId = env['GITHUB_SHA']
def repoUrl = env['GITHUB_REPOSITORY']
String scanComment = "Repo: repoUrl | Branch: $branch | Commit ID: $commitId"

println "INFO : Scanning code from $scanComment"

return scanComment
