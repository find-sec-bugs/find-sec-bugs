package testcode.sqli;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

public class CustomInjection {

    public void testQueries(DataSource dataSource, String input) throws SQLException {
        String sql = "select * from Users where name = " + input;
        Connection connection = dataSource.getConnection();
        try {
            MySqlWrapper wrapper = new MySqlWrapper(connection);
            ResultSet resultSet = wrapper.executeQuery(sql);
            System.out.println(resultSet.next());
        } finally {
            connection.close();
        }

    }
}

class MySqlWrapper {

    Connection connection;

    MySqlWrapper(Connection connection) {
        this.connection = connection;
    }

    ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            return statement.executeQuery(sql);
        } finally {
            statement.close();
        }
    }
}