package org.springframework.jdbc.core;

import java.sql.SQLException;

public interface BatchPreparedStatementSetter {
    int getBatchSize() throws SQLException;
    void setValues(java.sql.PreparedStatement ps, int i) throws SQLException;
}
