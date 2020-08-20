package testcode.bugs;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class OutOfBoundsLocalVariableIndex556 {
    private static PrintStream originalOut;

    /** utility class */
    private OutOfBoundsLocalVariableIndex556() {
    }

    public static synchronized void start(String filename) throws FileNotFoundException {
        if (originalOut == null) {
            PrintStream currentPS = System.out;
            originalOut = currentPS;

            FileOutputStream fos = new FileOutputStream(filename);
            // OutputStreamWriter osw = new OutputStreamWriter(fos,encoding);
            // BufferedWriter bw = new BufferedWriter(osw);
            // PrintWriter pw = new PrintWriter(bw);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
        } else {
            throw new IllegalStateException("RedirectOut already in used");
        }
    }

    public static synchronized void stop() {

        if (originalOut != null) {
            System.setOut(originalOut);
        } else {
            throw new IllegalStateException("RedirectOut not in use");
        }
    }
}
