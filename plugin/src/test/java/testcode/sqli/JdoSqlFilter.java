package testcode.sqli;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

public class JdoSqlFilter {

    private static final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public static PersistenceManager getPM() {
        return pmfInstance.getPersistenceManager();
    }

    //Risky..
    public void testJdoUnsafeFilter(String filterValue) {
        PersistenceManager pm = getPM();
        Query q = pm.newQuery(UserEntity.class);
        q.setFilter("id == "+filterValue);
    }

    //OK!
    public void testJdoSafeFilter(String filterValue) {
        PersistenceManager pm = getPM();
        Query q = pm.newQuery(UserEntity.class);
        q.setFilter("id == 1");
    }

    //OK!
    public void testJdoSafeFilter2(String filterValue) {
        PersistenceManager pm = getPM();
        Query q = pm.newQuery(UserEntity.class);
        q.setFilter("id == userId");
        q.declareParameters("int userId");

    }

    //TODO: The description should recommend the usage of declared parameters such as : http://stackoverflow.com/questions/3742221/jdo-exception-query-requires-1-parameters-yet-2-values-have-been-provided

    private static final String FIELD_TEST = "test";

    //Risky..
    public void testJdoUnsafeGrouping(String groupByField) {
        PersistenceManager pm = getPM();
        Query q = pm.newQuery(UserEntity.class);
        q.setGrouping(groupByField);
    }

    //OK!
    public void testJdoSafeGrouping() {
        PersistenceManager pm = getPM();
        Query q = pm.newQuery(UserEntity.class);
        q.setGrouping(FIELD_TEST);
    }


}
