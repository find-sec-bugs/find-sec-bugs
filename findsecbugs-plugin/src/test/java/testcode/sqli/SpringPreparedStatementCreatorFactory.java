package testcode.sqli;

import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Types;
import java.util.ArrayList;

public class SpringPreparedStatementCreatorFactory {


    public void queryUnsafe(String input) {
        String sql = "select * from Users where name = '" + input + "' id=?";

        new PreparedStatementCreatorFactory(sql);
        new PreparedStatementCreatorFactory(sql, new int[] {Types.INTEGER});
        new PreparedStatementCreatorFactory(sql, new ArrayList<SqlParameter>());
    }
}
