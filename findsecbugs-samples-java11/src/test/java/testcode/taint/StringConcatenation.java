package testcode.taint;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Java 11 String concaenation uses invokedynamic by default.
 */
public class StringConcatenation {

    // SQL

    public static boolean unsafeStringSqlConcatenation(Connection conn, String flag) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT " + flag + " FROM foo;");
        }
    }

    public static boolean safeBooleanSqlConcatenation(Connection conn, boolean flag) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT " + (flag ? "*" : "1") + " FROM foo;");
        }
    }

    public static boolean safeStringSqlConcatenation(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT " + Integer.toString(10000) + " FROM bar;");
        }
    }

    public static boolean safeIntSqlConcatenation(Connection conn, int foo) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT " + foo + " FROM bar;");
        }
    }

    public static boolean safeIntLongSqlConcatenation(Connection conn, int foo, long bar) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT " + foo + ", " + bar + " FROM bar;");
        }
    }

    public static boolean safeIntDoubleSqlConcatenation(Connection conn, int foo, double bar) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute("SELECT " + foo + ", " + bar + " FROM bar;");
        }
    }

    // File

    public static FileInputStream unsafeCharFileConcatenation(char foo) throws FileNotFoundException {
        return new FileInputStream("Hello" + foo);
    }

    public static FileInputStream unsafeStringFileConcatenation(String file) throws FileNotFoundException {
        return new FileInputStream("Hello" + file);
    }

    public static FileInputStream safeStringFileConcatenation(boolean flag) throws FileNotFoundException {
        return new FileInputStream("some" + (flag ? "Temp " : "") + "File");
    }

    public static FileInputStream safeIntFileConcatenation(int foo) throws FileNotFoundException {
        return new FileInputStream("some" + foo + "File");
    }

    public static FileInputStream safeStringLongFileConcatenation(int foo, long bar) throws FileNotFoundException {
        return new FileInputStream("some" + Integer.toString(foo) + "_" + bar + "File");
    }

    public static FileInputStream safeStringDoubleFileConcatenation(String blah, int foo, double bar) throws FileNotFoundException {
        return new FileInputStream("some" + "blah" + "_"  + bar + ", " + Integer.toString(foo) + "File");
    }
}
