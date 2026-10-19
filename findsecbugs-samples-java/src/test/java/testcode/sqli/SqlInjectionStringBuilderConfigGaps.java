package testcode.sqli;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Follow-up to https://github.com/find-sec-bugs/find-sec-bugs/issues/764
 *
 * The chained-append fix added #N mutable-argument markers to the single-argument
 * primitive append() overloads, but left two related gaps in java-lang.txt:
 *
 *  1. append(char[]), append(char[],int,int), appendCodePoint(int), insert(int,char[])
 *     and insert(int,char[],int,int) have NO taint-config entry at all on either
 *     StringBuilder or StringBuffer, so a tainted argument's taint is simply dropped.
 *  2. The single-argument primitive insert() overloads (Z/C/D/F/I/J) merge the
 *     receiver's taint but, unlike the now-fixed append() overloads, still lack the
 *     #N writeback marker, so they reproduce the exact chained-call bug issue #764
 *     fixed for append() when used as the first link of a chain.
 */
public class SqlInjectionStringBuilderConfigGaps {
    private static Connection _conn;

    protected static Connection getConnection() {
        return _conn;
    }

    public static void appendCharArrayChained(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            sql.append(input.toCharArray());
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    public static void appendCharArrayRangeChained(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            char[] chars = input.toCharArray();
            sql.append(chars, 0, chars.length);
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    public static void appendCodePointChained(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            sql.appendCodePoint(65).append(input);
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    public static void insertCharArrayChained(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            sql.insert(0, input.toCharArray());
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    public static void insertCharArrayRangeChained(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            char[] chars = input.toCharArray();
            sql.insert(0, chars, 0, chars.length);
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    public static void chainedInsertPrimitiveTaintedSecond(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            sql.insert(0, 'a').append(input);
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }
}
