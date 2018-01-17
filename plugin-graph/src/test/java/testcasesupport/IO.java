package testcasesupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class IO {

    /* fill in these parameters if you want to be able to actually connect
    * to a database
    */
    private static final String dbUrl = "";
    private static final String dbUsername = "";
    private static final String dbPassword = "";

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

    /* use this method to get a database connection for use in SQL
    * Injection and other test cases that use a database.
    */
    public static Connection getDBConnection() throws SQLException
    {
        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    /* The variables below are declared "final", so a tool doing whole
    program analysis should be able to identify that reads of these
    will always return their initialized values. */
    public static final boolean STATIC_FINAL_TRUE = true;
    public static final boolean STATIC_FINAL_FALSE = false;
    public static final int STATIC_FINAL_FIVE = 5;

    /* The variables below are not defined as "final", but are never
    assigned any other value, so a tool doing whole program analysis
    should be able to identify that reads of these will always return
    their initialized values. */
    public static boolean staticTrue = true;
    public static boolean staticFalse = false;
    public static int staticFive = 5;

    public static boolean staticReturnsTrue()
    {
        return true;
    }

    public static boolean staticReturnsFalse()
    {
        return false;
    }

    public static boolean staticReturnsTrueOrFalse()
    {
        return (new java.util.Random()).nextBoolean();
    }

    /* Turns array of bytes into string.  Taken from:
    http://java.sun.com/developer/technicalArticles/Security/AES/AES_v1.html */
    public static String toHex (byte byteBuffer[])
    {
        StringBuffer strBuffer = new StringBuffer(byteBuffer.length * 2);
        int i;

        for (i = 0; i < byteBuffer.length; i++)
        {
            if (((int) byteBuffer[i] & 0xff) < 0x10)
            {
                strBuffer.append("0");
            }

            strBuffer.append(Long.toString((int) byteBuffer[i] & 0xff, 16));
        }

        return strBuffer.toString();
    }
}
