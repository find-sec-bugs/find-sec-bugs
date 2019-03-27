package org.springframework.jdbc.core;

import org.springframework.dao.DataAccessException;

import java.sql.SQLException;

public interface ResultSetExtractor<T> {
    T extractData(java.sql.ResultSet rs) throws SQLException, DataAccessException;
}
