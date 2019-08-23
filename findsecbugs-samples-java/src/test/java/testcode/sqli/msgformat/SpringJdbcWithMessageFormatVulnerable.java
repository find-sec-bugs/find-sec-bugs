package testcode.sqli.msgformat;

import org.springframework.jdbc.core.JdbcTemplate;

import java.text.MessageFormat;

public class SpringJdbcWithMessageFormatVulnerable {

    private JdbcTemplate template = new JdbcTemplate();
    private static final String idName = "myid";

    public void testInjectionMessageFormat1(final Long id,String userTable) {
        // findsecbugs KO
        final String sql = MessageFormat.format("DELETE {0} WHERE {1}=?", userTable, idName);
        this.template.update(sql, id);
    }


    public void testInjectionMessageFormat2(final Long id,String userTable) {
        // findsecbugs KO
        final String sql = MessageFormat.format("DELETE "+userTable+" WHERE {0}=?", idName);
        this.template.update(sql, id);
    }
}
