package testcode.sqli;

import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.SqlConnection;

public class VertxSqlClient {

    public void injection1(SqlClient client, String injection) {
        client.query(injection);
    }

    public void injection2(SqlClient client, String injection) {
        client.preparedQuery(injection);
    }

    public void injection3(SqlConnection conn, String injection) {
        conn.prepare(injection);
    }

    public void injection4(SqlConnection conn, String injection) {
        conn.prepare(injection, null);
    }

    public void falsePositive1(SqlClient client) {
        String constantValue = "SELECT * FROM test";
        client.query(constantValue);
    }

    public void falsePositive2(SqlConnection conn) {
        String constantValue = "SELECT * FROM test";
        conn.query(constantValue);
    }
}
