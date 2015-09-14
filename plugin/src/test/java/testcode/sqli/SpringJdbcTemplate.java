package testcode.sqli;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Variation of SpringJdbcOperations.
 * Where a call on the concrete class is made instead of the interface.
 */
public class SpringJdbcTemplate {

    JdbcTemplate jdbcTemplate;

    public void query1(String input) throws DataAccessException {
        jdbcTemplate.execute("select * from Users where name = '"+input+"'");
    }

    public void query2(String input) throws DataAccessException {
        String sql = "select * from Users where name = '" + input + "'";
        jdbcTemplate.execute(sql);
    }

    public void query3(String input) throws DataAccessException {
        jdbcTemplate.execute(String.format("select * from Users where name = '%s'",input));
    }

    public void query4(String input) throws DataAccessException {
        String sql = "select * from Users where name = '%s'";
        jdbcTemplate.execute(String.format(sql,input));
    }

    public void querySafe(String input) throws DataAccessException {
        String sql = "select * from Users where name = '1'";
        jdbcTemplate.execute(sql);
    }

    public void queryExecute(String sql) throws DataAccessException {
        //Execute (4 signatures)
        jdbcTemplate.execute(sql);
        jdbcTemplate.execute(new StoredProcCall(sql), new TestCallableStatementCallback()); //The injection is flag later (line 107)
        jdbcTemplate.execute(sql, (PreparedStatementCallback) new TestCallableStatementCallback());
        jdbcTemplate.execute(sql, new TestCallableStatementCallback());
    }

    public void queryBatchUpdate(String sql) throws DataAccessException {
        //Batch Update (8 signatures)
        jdbcTemplate.batchUpdate(sql);
        jdbcTemplate.batchUpdate(sql, sql); //Same signature as the previous
        jdbcTemplate.batchUpdate("select * from dual", sql); //
        jdbcTemplate.batchUpdate(sql, "select * from dual"); //
        jdbcTemplate.batchUpdate(sql, new TestBatchPreparedStatementSetter());
        jdbcTemplate.batchUpdate(sql, new ArrayList<UserEntity>(), 11, new TestParameterizedPreparedStatementSetter());
        jdbcTemplate.batchUpdate(sql, new ArrayList<Object[]>());
        jdbcTemplate.batchUpdate(sql, new ArrayList<Object[]>(), new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR});
    }

    public void queryForObject(String sql) throws DataAccessException {
        //Query for Object (8 signatures)
        jdbcTemplate.queryForObject(sql, new TestRowMapper());
        jdbcTemplate.queryForObject(sql, new TestRowMapper(), "", "");
        jdbcTemplate.queryForObject(sql, UserEntity.class);
        jdbcTemplate.queryForObject(sql, UserEntity.class, "", "");
        jdbcTemplate.queryForObject(sql, new Object[0], UserEntity.class);
        jdbcTemplate.queryForObject(sql, new Object[0], new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR}, UserEntity.class);
        jdbcTemplate.queryForObject(sql, new Object[0], new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR}, new TestRowMapper());
        jdbcTemplate.queryForObject(sql, new Object[0], new TestRowMapper());
    }

    public void querySamples(String sql) throws DataAccessException {
        //Query (12 signatures)
        jdbcTemplate.query(sql, new TestResultSetExtractor());
        jdbcTemplate.query(sql, new TestRowCallbackHandler());
        jdbcTemplate.query(sql, new TestRowMapper());
        jdbcTemplate.query(sql, new TestPreparedStatementSetter(), new TestResultSetExtractor());
        jdbcTemplate.query(sql, new TestPreparedStatementSetter(), new TestRowCallbackHandler());
        jdbcTemplate.query(sql, new TestPreparedStatementSetter(), new TestRowMapper());
        jdbcTemplate.query(sql, new Object[0], new TestRowMapper());
        jdbcTemplate.query(sql, new Object[0], new TestRowCallbackHandler());
        jdbcTemplate.query(sql, new Object[0], new TestResultSetExtractor());
        jdbcTemplate.query(sql, new Object[0], new int[]{Types.VARCHAR}, new TestResultSetExtractor());
        jdbcTemplate.query(sql, new Object[0], new int[]{Types.VARCHAR}, new TestRowMapper());
        jdbcTemplate.query(sql, new Object[0], new int[]{Types.VARCHAR}, new TestRowCallbackHandler());
    }

    public void queryForList(String sql) throws DataAccessException {
        //Query for List (6 signatures)
        jdbcTemplate.queryForList(sql);
        jdbcTemplate.queryForList(sql, UserEntity.class);
        jdbcTemplate.queryForList(sql, new Object[0], UserEntity.class);
        jdbcTemplate.queryForList(sql, new Object[0], new int[]{Types.VARCHAR});
        jdbcTemplate.queryForList(sql, new Object[0], new int[]{Types.VARCHAR}, UserEntity.class);
        jdbcTemplate.queryForList(sql, new Object[0]);
    }

    public void queryForMap(String sql) throws DataAccessException {
        //Query for Map (3 signatures)
        jdbcTemplate.queryForMap(sql);
        jdbcTemplate.queryForMap(sql, new Object[0]);
        jdbcTemplate.queryForMap(sql, new Object[0], new int[]{Types.VARCHAR});
    }

    public void queryForRowSet(String sql) throws DataAccessException {
        //Query for Row Set (3 signatures)
        jdbcTemplate.queryForRowSet(sql);
        jdbcTemplate.queryForRowSet(sql, new Object[0]);
        jdbcTemplate.queryForRowSet(sql, new Object[0], new int[]{Types.VARCHAR});
    }


    public void queryForInt(String sql) throws DataAccessException {
        //Query for int (3 signatures)
        jdbcTemplate.queryForInt(sql);
        jdbcTemplate.queryForInt(sql, new Object[0]);
        jdbcTemplate.queryForInt(sql, new Object[0], new int[]{Types.VARCHAR});
    }

    public void queryForLong(String sql) throws DataAccessException {
        //Query for long (3 signatures)
        jdbcTemplate.queryForLong(sql);
        jdbcTemplate.queryForLong(sql, new Object[0]);
        jdbcTemplate.queryForLong(sql, new Object[0], new int[]{Types.VARCHAR});
    }

    //Bunch of Mock classes

    public class StoredProcCall implements CallableStatementCreator {

        private String sql;

        public StoredProcCall(String sql) {
            this.sql = sql;
        }

        @Override
        public CallableStatement createCallableStatement(Connection arg0)
                throws SQLException {
            CallableStatement cs=arg0.prepareCall(sql); //JDBC API
            cs.setString(1, "Param1");
            return cs;
        }
    }

    public class TestCallableStatementCallback<UserEntity> implements CallableStatementCallback<UserEntity> {
        @Override
        public UserEntity doInCallableStatement(CallableStatement callableStatement) throws SQLException, DataAccessException {
            return null;
        }
    }

    public class TestParameterizedPreparedStatementSetter<UserEntity> implements ParameterizedPreparedStatementSetter<UserEntity> {

        @Override
        public void setValues(PreparedStatement preparedStatement, UserEntity o) throws SQLException {

        }
    }

    public class TestBatchPreparedStatementSetter<UserEntity> implements BatchPreparedStatementSetter {

        @Override
        public void setValues(PreparedStatement var1, int var2) throws SQLException {

        }

        @Override
        public int getBatchSize() {
            return 10;
        }
    }
    public class TestRowMapper<UserEntity> implements RowMapper<UserEntity> {

        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return null;
        }
    }
    public class TestResultSetExtractor<UserEntity> implements ResultSetExtractor<UserEntity> {

        @Override
        public UserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
            return null;
        }
    }

    public class TestPreparedStatementSetter implements PreparedStatementSetter {

        @Override
        public void setValues(PreparedStatement ps) throws SQLException {

        }
    }

    public class TestRowCallbackHandler implements RowCallbackHandler {

        @Override
        public void processRow(ResultSet rs) throws SQLException {

        }
    }
}
