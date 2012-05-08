package edu.umd.cs.findbugs;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.apache.commons.io.IOUtils;

import edu.umd.cs.findbugs.config.UserPreferences;

public class BaseDetectorTest {
	private static final boolean DEBUG = true;

    /**
     * @param path
     * @return Full path to the class file base on class name.
     */
    public String getClassFilePath(String path) {
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource(path+".class");

        String filename = url.toString();

        final String prefix = "file:/";
        if(filename.startsWith(prefix)) {
            filename = filename.substring(prefix.length());
        }
        return filename;
    }

	/**
	 * Launch an analysis on the given source directory.
	 * 
	 * @param classFiles
	 * @param bugReporter
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws PluginException
	 */
	public void analyze(String[] classFiles, BugReporter bugReporter) throws IOException, InterruptedException,
            PluginException, NoSuchFieldException, IllegalAccessException {

		final ClassLoader cl = getClass().getClassLoader();

		Project project = new Project();
		project.setProjectName("automate-test-project");
		for(String file : classFiles) {
			project.addFile(file);
		}
		
		//Initialize the plugin base on the findbugs.xml
		byte[] archive = buildFakePluginJar();

        File f = new File(System.getProperty("java.io.tmpdir"),"plugin.jar");
        System.out.println("Writing "+f.getCanonicalPath());
        f.deleteOnExit();
        FileOutputStream out = new FileOutputStream(f);
        out.write(archive);
        out.close();

		//URL pluginUrl = urlMockFromInputStream(new ByteArrayInputStream(archive));
		Plugin loadedPlugin = Plugin.loadCustomPlugin(f.toURL(),project);
		
		//FindBugs engine
		
		FindBugs2 engine = new FindBugs2();
		engine.setNoClassOk(true);
		engine.setMergeSimilarWarnings(false);
		engine.setBugReporter(bugReporter);
		engine.setProject(project);
		engine.setProgressCallback(mock(FindBugsProgress.class));
		
		engine.setDetectorFactoryCollection(DetectorFactoryCollection.instance());

		//User preferences set to miss no bugs report
		UserPreferences prefs = UserPreferences.createDefaultUserPreferences();
		prefs.getFilterSettings().setMinRank(20);
		prefs.getFilterSettings().setDisplayFalseWarnings(true);
		prefs.getFilterSettings().setMinPriority("Low");
		engine.setUserPreferences(prefs);
		
        
    System.out.println("Analyzing...");
		engine.execute();

	}
	
	/**
	 * The minimum requirement to have a "valid" archive plugin is to include 
	 * a findbugs.xml, messages.xml and MANIFEST.MF files. The rest of the 
	 * resources are load using the parent ClassLoader (Not requires to be in 
	 * the jar).
	 * <p>
	 * Instead of building a file on disk, the result of the stream is kept in
	 * memory and return as a byte array. 
	 * @return
	 * @throws IOException 
	 */
	private byte[] buildFakePluginJar() throws IOException {
		ClassLoader cl = getClass().getClassLoader();
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		JarOutputStream jar = new JarOutputStream(buffer);
        //
		for(String resource : Arrays.asList("findbugs.xml","messages.xml","META-INF/MANIFEST.MF")) {
			jar.putNextEntry(new ZipEntry(resource));
			jar.write(IOUtils.toByteArray( cl.getResourceAsStream("metadata/"+resource)));
		}
		jar.finish();
		
		return buffer.toByteArray();
	}
	
	/**
	 * URL is a system class that can't be mock using common mocking library.
	 * Instead a URL instance with a special handler is build.
	 * 
	 * @param is
	 * @return URL instance
	 * @throws IOException
	 */
	private URL urlMockFromInputStream(InputStream is) throws IOException {
		final URLConnection mockUrlCon = mock(URLConnection.class);
		when(mockUrlCon.getInputStream()).thenReturn(is);
		
		URLStreamHandler stubUrlHandler = new URLStreamHandler() {
		    @Override
		     protected URLConnection openConnection(URL u) throws IOException {
		        return mockUrlCon;
		     }
		};
		
		URL url = new URL("proto", "host", 99, "/findbugs", stubUrlHandler);
		return url;
	}

}
