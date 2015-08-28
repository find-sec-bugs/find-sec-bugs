package org.springframework.jdbc.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public interface CallableStatementCreator {

    CallableStatement createCallableStatement(Connection arg0) throws SQLException;
}
