package testcode.sqli;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;

/**
 * Reproduces https://github.com/find-sec-bugs/find-sec-bugs/issues/764
 *
 * SQL_INJECTION_JDBC was missed when a tainted value is appended as the second,
 * chained argument of a StringBuilder.append() call, for example
 * sql.append(',').append(tainted). The intermediate append() return value sits
 * on the operand stack and has no local variable index, so the taint written
 * back by the second append() never reached the original StringBuilder local.
 * The single-argument primitive append() summaries did not mark the builder as
 * a mutable argument, which is what anchored the writeback to the variable.
 */
public class SqlInjectionStringBuilderChained764 {
    private static Connection _conn;

    protected static Connection getConnection() {
        return _conn;
    }

    public static void usesIteratorOfIntegersWithChainedAppendItemFirst(Collection<Integer> ids) {
        Connection conn = null;
        PreparedStatement ps = null;

        StringBuilder sql = new StringBuilder("UPDATE table SET field=0 WHERE id IN (NULL, ");
        for (Iterator<Integer> iterator = ids.iterator(); iterator.hasNext(); ) {
            sql.append(iterator.next()).append(',');
        }
        sql.append(')');

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        } finally {
            close(conn, ps);
        }
    }

    public static void usesIteratorOfIntegersWithChainedAppendItemSecond(Collection<Integer> ids) {
        Connection conn = null;
        PreparedStatement ps = null;

        StringBuilder sql = new StringBuilder("UPDATE table SET field=0 WHERE id IN (NULL, ");
        for (Iterator<Integer> iterator = ids.iterator(); iterator.hasNext(); ) {
            sql.append(',').append(iterator.next());
        }
        sql.append(')');

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        } finally {
            close(conn, ps);
        }
    }

    public static void usesIteratorOfIntegersWithSeparateAppendItemFirst(Collection<Integer> ids) {
        Connection conn = null;
        PreparedStatement ps = null;

        StringBuilder sql = new StringBuilder("UPDATE table SET field=0 WHERE id IN (NULL, ");
        for (Iterator<Integer> iterator = ids.iterator(); iterator.hasNext(); ) {
            sql.append(iterator.next());
            sql.append(',');
        }
        sql.append(')');

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        } finally {
            close(conn, ps);
        }
    }

    public static void usesIteratorOfIntegersWithSeparateAppendItemSecond(Collection<Integer> ids) {
        Connection conn = null;
        PreparedStatement ps = null;

        StringBuilder sql = new StringBuilder("UPDATE table SET field=0 WHERE id IN (NULL, ");
        for (Iterator<Integer> iterator = ids.iterator(); iterator.hasNext(); ) {
            sql.append(',');
            sql.append(iterator.next());
        }
        sql.append(')');

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        } finally {
            close(conn, ps);
        }
    }

    // Minimal, loop-free variants that isolate the chained-append behaviour.

    public static void chainedAppendTaintedFirst(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            sql.append(input).append(',');
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    public static void chainedAppendTaintedSecond(String input) {
        try {
            StringBuilder sql = new StringBuilder("SELECT 1 WHERE x=");
            sql.append(',').append(input);
            getConnection().prepareStatement(sql.toString()).executeUpdate();
        } catch (SQLException sqle) {
            // Do nothing
        }
    }

    private static void close(Connection conn, Statement s) {
        if (null != s) try { s.close(); } catch (SQLException sqle) { }
        if (null != conn) try { conn.close(); } catch (SQLException sqle) { }
    }
}
