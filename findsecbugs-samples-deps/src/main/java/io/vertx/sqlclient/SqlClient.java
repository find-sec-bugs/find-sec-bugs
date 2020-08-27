package io.vertx.sqlclient;

public interface SqlClient {

    Query<RowSet<Row>> query(String sql);

    PreparedQuery<RowSet<Row>> preparedQuery(String sql);
}
