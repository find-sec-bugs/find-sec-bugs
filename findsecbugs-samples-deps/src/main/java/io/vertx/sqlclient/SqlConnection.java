package io.vertx.sqlclient;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public interface SqlConnection extends SqlClient {

    SqlConnection prepare(String sql, Handler<AsyncResult<PreparedStatement>> handler);

    Future<PreparedStatement> prepare(String sql);
}
