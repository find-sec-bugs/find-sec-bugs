package testcode.sqli;

import org.springframework.jdbc.core.BatchUpdateUtils;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterBatchUpdateUtils;

import java.sql.Types;
import java.util.ArrayList;

public class SpringBatchUpdateUtils {

    JdbcOperations jdbcOperations;

    public void queryBatchUpdateUnsafe(String input) {
        String sql = "UPDATE Users SET name = '"+input+"' where id = 1";
        BatchUpdateUtils.executeBatchUpdate(sql, new ArrayList<Object[]>(),new int[] {Types.INTEGER}, jdbcOperations);
    }

    public void queryBatchUpdateSafe() {
        String sql = "UPDATE Users SET name = 'safe' where id = 1";
        BatchUpdateUtils.executeBatchUpdate(sql, new ArrayList<Object[]>(),new int[] {Types.INTEGER}, jdbcOperations);
    }

    public void queryNamedParamBatchUpdateUnsafe(String input) {
        String sql = "UPDATE Users SET name = '"+input+"' where id = 1";
        NamedParameterBatchUpdateUtils.executeBatchUpdate(sql, new ArrayList<Object[]>(),new int[] {Types.INTEGER}, jdbcOperations);
    }

    public void queryNamedParameterBatchUpdateUtilsSafe() {
        String sql = "UPDATE Users SET name = 'safe' where id = 1";
        NamedParameterBatchUpdateUtils.executeBatchUpdate(sql, new ArrayList<Object[]>(), new int[]{Types.INTEGER}, jdbcOperations);
    }
}
