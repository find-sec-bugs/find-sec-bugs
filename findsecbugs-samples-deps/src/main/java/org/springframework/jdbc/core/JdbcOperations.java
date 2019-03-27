package org.springframework.jdbc.core;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface JdbcOperations {

    //-------------------------------------------------------------------------
    // Methods dealing with static SQL (java.sql.Statement)
    //-------------------------------------------------------------------------

    <T> T execute(StatementCallback<T> action) throws DataAccessException;

    void execute(String sql) throws DataAccessException;

    <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException;

    void query(String sql, RowCallbackHandler rch) throws DataAccessException;

    <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException;

    Map<String, Object> queryForMap(String sql) throws DataAccessException;

    <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException;

    List<Map<String, Object>> queryForList(String sql) throws DataAccessException;

    SqlRowSet queryForRowSet(String sql) throws DataAccessException;

    /**
     * Issue a single SQL update operation (such as an insert, update or delete statement).
     * @param sql static SQL to execute
     * @return the number of rows affected
     * @throws DataAccessException if there is any problem.
     */
    int update(String sql) throws DataAccessException;

    /**
     * Issue multiple SQL updates on a single JDBC Statement using batching.
     * <p>Will fall back to separate updates on a single Statement if the JDBC
     * driver does not support batch updates.
     * @param sql defining an array of SQL statements that will be executed.
     * @return an array of the number of rows affected by each statement
     * @throws DataAccessException if there is any problem executing the batch
     */
    int[] batchUpdate(String... sql) throws DataAccessException;


    //-------------------------------------------------------------------------
    // Methods dealing with prepared statements
    //-------------------------------------------------------------------------

    <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException;

    <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException;

    <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException;

    <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException;

    <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException;

    <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException;

    <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException;

    void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException;

    void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException;

    void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException;

    void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException;

    void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException;

    <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

    <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException;

    <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
            throws DataAccessException;

    <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException;

    <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException;

    <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
            throws DataAccessException;

    <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException;

    <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException;

    Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException;

    Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException;

    <T>List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType)
            throws DataAccessException;

    <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException;

    <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException;

    List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException;

    List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException;

    SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException;

    SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException;

//    int update(PreparedStatementCreator psc) throws DataAccessException;
//
//    int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException;

    int update(String sql, PreparedStatementSetter pss) throws DataAccessException;

    int update(String sql, Object[] args, int[] argTypes) throws DataAccessException;

    int update(String sql, Object... args) throws DataAccessException;

    int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException;

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws DataAccessException;

    public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) throws DataAccessException;

    public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize,
                                   ParameterizedPreparedStatementSetter<T> pss) throws DataAccessException;


    //-------------------------------------------------------------------------
    // Methods dealing with callable statements
    //-------------------------------------------------------------------------

    <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException;

    <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException;

    Map<String, Object> call(CallableStatementCreator csc, List<SqlParameter> declaredParameters)
            throws DataAccessException;



    //Spring 3.2.1
    //http://docs.spring.io/spring-framework/docs/2.0.x/api/org/springframework/jdbc/core/JdbcTemplate.html

    int queryForInt(String sql);
    int queryForInt(String sql, Object[] args);
    int queryForInt(String sql, Object[] args, int[] argTypes);


    long queryForLong(String sql);
    long queryForLong(String sql, Object[] args);
    long queryForLong(String sql, Object[] args, int[] argTypes);

}
