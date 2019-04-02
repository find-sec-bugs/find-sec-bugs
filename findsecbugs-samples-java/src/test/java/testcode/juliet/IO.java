package testcode.juliet;

import java.util.logging.Logger;

public class IO {
    public static final Logger logger = Logger.getLogger("testcases");

    public static void writeString(String str)
    {
        System.out.print(str);
    }

    public static void writeLine(String line)
    {
        System.out.println(line);
    }

    public static void writeLine(int intNumber)
    {
        writeLine(String.format("%02d", intNumber));
    }

    public static void writeLine(long longNumber)
    {
        writeLine(String.format("%02d", longNumber));
    }

    public static void writeLine(double doubleNumber)
    {
        writeLine(String.format("%02f", doubleNumber));
    }

    public static void writeLine(float floatNumber)
    {
        writeLine(String.format("%02f", floatNumber));
    }

    public static void writeLine(short shortNumber)
    {
        writeLine(String.format("%02d", shortNumber));
    }

    public static void writeLine(byte byteHex)
    {
        writeLine(String.format("%02x", byteHex));
    }


    public static boolean staticReturnsTrueOrFalse()
    {
        return (new java.util.Random()).nextBoolean();
    }
}
