package org.springframework.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterizedPreparedStatementSetter<T> {
    void setValues(PreparedStatement preparedStatement, T o) throws SQLException;
}
