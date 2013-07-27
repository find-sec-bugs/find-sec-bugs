package testcode;

import java.io.*;

public class PathTraversal {

    public static void main(String[] args) throws IOException {
        String input = args.length > 0 ? args[0] : "../../../../etc/password\u0000";

        new File(input);
        new File("test/" + input, "misc.jpg");

        new RandomAccessFile(input, "r");

        new FileReader(input);

        new FileInputStream(input);


        new FileWriter(input);
        new FileWriter(input, true);

        new FileOutputStream(input);
        new FileOutputStream(input, true);
    }
}
