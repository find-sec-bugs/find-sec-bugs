package testcode.sqli.msgformat;

import java.text.MessageFormat;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 */
public class SpringJdbcWithMessageFormatFP {

    private JdbcTemplate template = new JdbcTemplate();
    private static final String tableName = "mytable";
    private static final String idName = "myid";

    public void testInjectionMessageFormat(final Long id) {
        // findsecbugs KO
        final String sql = MessageFormat.format("DELETE {0} WHERE {1}=?", tableName, idName);
        this.template.update(sql, id);
    }

    public void testInjectionStringConcat(final Long id) {
        // findsecbugs OK
        final String sql = "DELETE " + tableName + " WHERE " + idName + "=?";
        this.template.update(sql, id);
    }
}
