package org.springframework.jdbc.core;

import org.springframework.dao.DataAccessException;

import java.sql.CallableStatement;
import java.sql.SQLException;

public interface CallableStatementCallback<T> {
    T doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException;
}
