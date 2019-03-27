package org.springframework.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowCallbackHandler {
    void processRow(ResultSet rs) throws SQLException;
}
