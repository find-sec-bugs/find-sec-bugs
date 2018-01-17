package testcode.special;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * com.opensymphony.xwork2.util.ClassPathFinder
 */
public class ClassPathFinderSample<E extends Object> {
    private String pattern ;
    private int[] compiledPattern ;

    private PatternMatcher<int[]> patternMatcher = null;
    private Vector<String> compared = new Vector<>();

    public Vector<String> findMatches() {
        Vector<String> matches = new Vector<>();
        URLClassLoader cl = getURLClassLoader();
        if (cl == null ) {
            throw new XWorkException("unable to attain an URLClassLoader") ;
        }
        URL[] parentUrls = cl.getURLs();
        compiledPattern = patternMatcher.compilePattern(pattern);
        for (URL url : parentUrls) {
            if (!"file".equals(url.getProtocol())) {
                continue ;
            }
            URI entryURI ;
            try {
                entryURI = url.toURI();
            } catch (URISyntaxException e) {
                continue;
            }
            File entry = new File(entryURI);
            if (entry.isFile() && entry.toString().endsWith(".jar")) {
                try {
                    ZipInputStream zip = new ZipInputStream(new FileInputStream(entry));
                    for (ZipEntry zipEntry = zip.getNextEntry(); zipEntry != null; zipEntry = zip.getNextEntry()) {
                        boolean doesMatch = patternMatcher.match(new HashMap<String, String>(), zipEntry.getName(), compiledPattern);
                        if (doesMatch) {
                            matches.add(zipEntry.getName());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Vector<String> results = checkEntries(entry.list(), entry, "");
                if (results != null) {
                    matches.addAll(results);
                }
            }
        }
        return matches;
    }

    private Vector<String> checkEntries(String[] entries, File parent, String prefix) {

        if (entries == null ) {
            return null;
        }

        Vector<String> matches = new Vector<>();
        for (String listEntry : entries) {
            File tempFile ;
            if (!"".equals(prefix) ) {
                tempFile = new File(parent, prefix + "/" + listEntry);
            }
            else {
                tempFile = new File(parent, listEntry);
            }
            if (tempFile.isDirectory() &&
                    !(".".equals(listEntry) || "..".equals(listEntry)) ) {
                if	(!"".equals(prefix) ) {
                    matches.addAll(checkEntries(tempFile.list(), parent, prefix + "/" + listEntry));
                }
                else {
                    matches.addAll(checkEntries(tempFile.list(), parent, listEntry));
                }
            }
            else {

                String entryToCheck ;
                if ("".equals(prefix)) {
                    entryToCheck = listEntry ;
                }
                else {
                    entryToCheck = prefix + "/" + listEntry ;
                }

                if (compared.contains(entryToCheck) ) {
                    continue;
                }
                else {
                    compared.add(entryToCheck) ;
                }

                boolean doesMatch = patternMatcher.match(new HashMap<String,String>(), entryToCheck, compiledPattern);
                if (doesMatch) {
                    matches.add(entryToCheck);
                }
            }
        }
        return matches ;
    }


    private URLClassLoader getURLClassLoader() {
        URLClassLoader ucl = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if(! (loader instanceof URLClassLoader)) {
            loader = ClassPathFinderSample.class.getClassLoader();
            if (loader instanceof URLClassLoader) {
                ucl = (URLClassLoader) loader ;
            }
        }
        else {
            ucl = (URLClassLoader) loader;
        }

        return ucl ;
    }
}

interface PatternMatcher<E extends Object> {

    boolean isLiteral(String pattern);
    E compilePattern(String data);
    boolean match(Map<String,String> map, String data, E expr);
}

class XWorkException extends RuntimeException {

    public XWorkException(String s) {
    }

}
