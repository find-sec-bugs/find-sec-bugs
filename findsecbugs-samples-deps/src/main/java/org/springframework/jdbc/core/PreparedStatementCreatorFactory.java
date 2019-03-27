package org.springframework.jdbc.core;

import java.util.List;

public class PreparedStatementCreatorFactory {

    public PreparedStatementCreatorFactory(String sql) {

    }

    public PreparedStatementCreatorFactory(String sql, int[] ints) {

    }

    public PreparedStatementCreatorFactory(String sql, List<SqlParameter> sqlParameters) {
    }

}
