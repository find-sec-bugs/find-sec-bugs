package testcode.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.servlet.http.HttpServletRequest;
public abstract class CommandInjection {
    public static HttpServletRequest req; //Could be override at any time. (tainted)
    public static void main(String[] args) throws IOException {
        String input = args.length > 0 ? args[0] : ";cat /etc/passwd";
        List<String> cmd = Arrays.asList("ls", "-l", input);
        //Runtime exec()
        Runtime r = Runtime.getRuntime();
        r.exec("ls -l " + input);
        r.exec("ls -l " + input, null);
        r.exec("ls -l " + input, null, null);
        r.exec(cmd.toArray(new String[cmd.size()]));
        r.exec(cmd.toArray(new String[cmd.size()]), null);
        r.exec(cmd.toArray(new String[cmd.size()]), null, null);
        //ProcessBuilder
        new ProcessBuilder()
                .command("ls", "-l", input)
                .start();
        new ProcessBuilder()
                .command(cmd)
                .start();
    }

    public void bad(String tainted) throws IOException {
        StringBuilder builder = new StringBuilder("<" + tainted + ">");
        builder.insert(3, tainted).append("");
        builder.reverse();
        StringBuilder builder2 = new StringBuilder("xxx");
        builder2.append("").append(builder);
        String safe = "yyy";
        String unsafe = safe.replace("y", builder2.toString());
        Runtime.getRuntime().exec(unsafe.toLowerCase().substring(1).intern());
    }

    public void good() throws IOException {
        String hardcoded = "constant";
        boolean b = "xxx".equals(hardcoded);
        StringBuilder builder = new StringBuilder("<" + hardcoded + ">");
        builder.insert(3, hardcoded).append("");
        builder.reverse();
        StringBuilder builder2 = b ? new StringBuilder("xxx") : new StringBuilder(8);
        builder2.append("").append(builder);
        String safe = "yyy";
        String unsafe = safe.replace("y", builder2.toString());
        Runtime.getRuntime().exec(unsafe.toLowerCase().substring(1).intern());
    }

    public void badWithException() throws Exception {
        String data = "";
        File file = new File("C:\\data.txt");
        FileInputStream streamFileInput;
        InputStreamReader readerInputStream;
        BufferedReader readerBuffered;
        try {
            streamFileInput = new FileInputStream(file);
            readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
            readerBuffered = new BufferedReader(readerInputStream);
            data = readerBuffered.readLine();
        } catch (IOException ex) {
        }
        Runtime.getRuntime().exec(data);
    }

    public void badInterMethod() throws Exception {
        Runtime.getRuntime().exec(taintSource(""));
    }

    public void goodInterMethod() throws Exception {
        Runtime.getRuntime().exec(safeSource(0));
    }

    public void badWithTaintSink() throws Exception {
        taintSink("safe", req.getHeader("x"));
    }

    private void taintSink(String param1, String param2) throws Exception {
        Runtime.getRuntime().exec(param1 + " safe " + param2);
    }

    public void badWithDoubleTaintSink() throws Exception {
        taintSinkTransfer(req.getParameter("y"));
    }

    public void taintSinkTransfer(String str) throws Exception {
        taintSink2(str.toLowerCase());
    }

    public static void taintSink2(String param) throws Exception {
        Runtime.getRuntime().exec(param);
    }

    public void badCombo() throws Exception {
        String str = taintSourceDouble().toUpperCase();
        str = str.concat("aaa" + "bbb");
        comboSink(new StringBuilder(str).substring(1));
    }

    public void comboSink(String str) throws Exception {
        Runtime.getRuntime().exec(str);
    }

    public void badTransfer() throws IOException {
        String tainted = req.getParameter("zzz");
        Runtime.getRuntime().exec(combine("safe", tainted));
    }

    public void goodTransfer() throws IOException {
        String safe = "zzz";
        Runtime.getRuntime().exec(combine("safe", safe));
    }

    public void interClass() throws IOException {
        Runtime.getRuntime().exec(MoreMethods.tainted());
        Runtime.getRuntime().exec((new MoreMethods()).safe());
    }

    public void unconfiguredObject() throws IOException {
        Runtime.getRuntime().exec(parametricUnknownSource("safe, but result unknown"));
    }

    public void stringArrays(String param) throws Exception {
        Runtime.getRuntime().exec(transferThroughArray(taintSource("")));
        Runtime.getRuntime().exec(transferThroughArray("const" + param));
        Runtime.getRuntime().exec(transferThroughArray("const"));
    }

    public void lists(String param) throws Exception {
        Runtime.getRuntime().exec(transferThroughList(taintSource(""), 0));
        Runtime.getRuntime().exec(transferThroughList("const" + param, 0));
        Runtime.getRuntime().exec(transferThroughList("const", 0));
    }

    abstract void unknown(String str, String s);
    abstract void unknown(StringBuilder sb);

    public void testUnknown() throws IOException {
        String str = "xx";
        unknown(str, "");
        Runtime.getRuntime().exec(str);
        StringBuilder sb = new StringBuilder("xx");
        unknown(sb);
        Runtime.getRuntime().exec(sb.toString());
    }

    public void testListIterator() throws IOException {
        Runtime.getRuntime().exec(transferListIteratorIndirect("safe"));
        Runtime.getRuntime().exec(transferListIteratorIndirect(taintSourceDouble()));
        Runtime.getRuntime().exec(transferThroughListIterator("safe"));
        Runtime.getRuntime().exec(transferThroughListIterator(taintSourceDouble()));
    }
    
    public void unknownSubmethod(String unknown) throws IOException {
        Runtime.getRuntime().exec(new MoreMethods().safeParentparametricChild(unknown));
        Runtime.getRuntime().exec(new SubClass().safeParentparametricChild(unknown));
    }
    
    public void sinkWithSafeInput(String str) throws IOException {
        Runtime.getRuntime().exec(str);
    }
    
    public void safeCall() throws IOException {
        sinkWithSafeInput("safe");
    }
    
    private String transferThroughArray(String in) {
        String[] strings = new String[3];
        strings[0] = "safe1";
        strings[1] = in;
        strings[2] = "safe2";
        // whole array is tainted, index is not important
        String str = "safe3" + strings[1].trim();
        return str.split("a")[0];
    }

    private String transferThroughList(String in, int index) {
        LinkedList<String> list = new LinkedList<String>();
        list.add(System.getenv("")); // taints the list
        list.clear(); // makes the list safe again
        list.add(1, "xx");
        list.addFirst(in); // can taint the list
        list.addLast("yy");
        list.push(in);
        return list.element() + list.get(index) + list.getFirst() + list.getLast()
                + list.peek() + list.peekFirst() + list.peekLast() + list.poll()
                + list.pollFirst() + list.pollLast() + list.pop() + list.remove()
                + list.remove(index) + list.removeFirst() + list.removeLast()
                + list.set(index, "safe") + list.toString();
    }
    
    private String transferThroughListIterator(String str) {
        List<String> list = new LinkedList<String>();
        ListIterator<String> listIterator = list.listIterator();
        listIterator.add(str);
        return listIterator.next();
    }
    
    private String transferListIteratorIndirect(String str) {
        List<String> list = new LinkedList<String>();
        // not able to transfer this, set as UNKNOWN even if str is SAFE
        ListIterator<String> listIterator = list.listIterator();
        listIterator.add(str);
        return list.get(0);
    }

    public String parametricUnknownSource(String str) {
        return str + new Object().toString() + "xx";
    }

    public String taintSource(String param) throws IOException {
        File file = new File("C:\\data.txt");
        FileInputStream streamFileInput;
        InputStreamReader readerInputStream;
        BufferedReader readerBuffered;
        streamFileInput = new FileInputStream(file);
        readerInputStream = new InputStreamReader(streamFileInput, "UTF-8");
        readerBuffered = new BufferedReader(readerInputStream);
        return param + readerBuffered.readLine();
    }

    public String taintSourceDouble() throws IOException {
        return taintSource("safe, but result will be tainted") + safeSource(1);
    }

    public String safeSource(long number) {
        String safe = 3.14 + (number * 2) + "xx".toUpperCase() + null + Integer.toString(7);
        safe.concat(new Double(10.0).toString() + Long.toHexString(number));
        StringBuilder sb = new StringBuilder(safe).insert(1, 'c');
        sb.append(new Integer(0)).append(Double.valueOf("1.0")); // object taint transfer
        return sb + String.valueOf(true) + 0.1f + new String();
    }

    public String combine(String x, String y) {
        StringBuilder sb = new StringBuilder("safe");
        sb.append((Object) x);
        HashSet<String> set = new HashSet<String>();
        set.add("ooo");
        set.add(sb.append("x").append("y").toString().toLowerCase());
        for (String str : set) {
            if (str.equals(y.toLowerCase())) {
                return str + String.join("-", set) + String.join("a", "b", "c");
            }
        }
        return new StringBuilder(String.format("%s", y)).toString().trim() + "a".concat("aaa");
    }

    public void call() throws IOException {
        MoreMethods.sink(req.getHeader("test"));
    }
    
    public void callInterface(InterfaceWithSink obj1) throws IOException {
        InterfaceWithSink obj2 = getNewMoreMethods();
        if (obj2.hashCode() % 2 == 0) {
            System.out.println(obj2.toString());
        } // just to confuse the analysis a bit
        unknown(new StringBuilder().append(obj2));
        obj2.sink2(req.getHeader("test"));
        obj1.sink2(req.getHeader("test")); // should not be reported
    }
    
    public InterfaceWithSink getNewMoreMethods() {
        return new MoreMethods();
    }
}
