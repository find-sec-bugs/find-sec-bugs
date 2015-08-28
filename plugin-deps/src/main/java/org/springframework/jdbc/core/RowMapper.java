package org.springframework.jdbc.core;

import java.sql.SQLException;

public interface RowMapper<T> {

    T mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException;
}
