package testcode.password;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcDriverConnection {

    private static final String MYSQL_PASSWORD = "h4rDc0de";

    public static void connectionWithHardCodePassword1() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://prod.company.com/production", "root", "lamepassword");
    }

    public static void connectionWithHardCodePassword2() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://prod.company.com/production", "dba", MYSQL_PASSWORD);
    }

    public static void connectionOK(Properties props) throws SQLException {
        String username = props.getProperty("username");
        String password = props.getProperty("password");
        Connection conn = DriverManager.getConnection("jdbc:mysql://prod.company.com/production", username, password);
    }
}
