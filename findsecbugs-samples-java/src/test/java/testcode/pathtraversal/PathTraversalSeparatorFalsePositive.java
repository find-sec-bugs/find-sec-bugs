package testcode.pathtraversal;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.FileSystems;

public class PathTraversalSeparatorFalsePositive {

    private static String configFileName = "test.properties";

    public static void main(String[] args){
        {
            String DIR1 = ".";
            File t1 = new File(DIR1, FilenameUtils.getName(configFileName)); // OK
            System.out.println(t1.toString());
        }
        {
            String DIR2 = "." + File.separator;
            File t2 = new File(DIR2, FilenameUtils.getName(configFileName)); // OK
            System.out.println(t2.toString());
        }
        {
            String DIR3 = "." + FileSystems.getDefault().getSeparator();
            File t3 = new File(DIR3, FilenameUtils.getName(configFileName)); // OK
            System.out.println(t3.toString());
        }
    }

    public static void danger(String input){
        {
            String DIR1 = ".";
            File t1 = new File(DIR1, input); // TAINTED
            System.out.println(t1.toString());
        }
        {
            String DIR2 = "." + File.separator;
            File t2 = new File(DIR2, input); // TAINTED
            System.out.println(t2.toString());
        }
        {
            String DIR3 = "." + FileSystems.getDefault().getSeparator();
            File t3 = new File(DIR3, input); // TAINTED
            System.out.println(t3.toString());
        }
    }
    
}
