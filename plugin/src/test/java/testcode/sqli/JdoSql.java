package testcode.sqli;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class JdoSql {

    private static final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");


    public static PersistenceManager getPM() {
        return pmfInstance.getPersistenceManager();
    }

    public void testJdoQueries(String input) {
        PersistenceManager pm = getPM();

        pm.newQuery("select * from Users where name = " + input);

        pm.newQuery("sql", "select * from Products where name = " + input);

        //Test for false positive

        pm.newQuery("select * from Config");

        final String query = "select * from Config";
        pm.newQuery(query);

        pm.newQuery("sql", query);
    }

}
