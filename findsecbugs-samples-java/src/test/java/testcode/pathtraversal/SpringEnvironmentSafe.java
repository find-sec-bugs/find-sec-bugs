package testcode.pathtraversal;

import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SpringEnvironmentSafe {

    private Environment env;

    /**
     * Using Environment.getProperty() to build a file path is safe because
     * the value comes from server-side configuration, not user input.
     */
    public void safePropertyUsage() throws FileNotFoundException {
        String basePath = env.getProperty("app.upload.dir");
        File file = new File(basePath, "report.pdf");
        new FileInputStream(file);
    }

    public void safePropertyWithDefault() throws FileNotFoundException {
        String basePath = env.getProperty("app.data.dir", "/tmp/data");
        File file = new File(basePath, "output.csv");
        new FileInputStream(file);
    }

    public void safeRequiredProperty() throws FileNotFoundException {
        String basePath = env.getRequiredProperty("app.storage.path");
        File file = new File(basePath, "archive.zip");
        new FileInputStream(file);
    }
}
