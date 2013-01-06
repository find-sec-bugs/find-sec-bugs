package testcode.sqli;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class HibernateSql {

    public void testQueries(SessionFactory sessionFactory, String input) {

        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(UserEntity.class);

        //The following would need to be audited

        criteria.add(Restrictions.sqlRestriction("com.h3xstream.findbugs.test=1234" + input));

        session.createQuery("select t from UserEntity t where id = " + input);

        session.createSQLQuery(String.format("select * from TestEntity where id = %s ", input));


        //OK nothing risky here..

        criteria.add(Restrictions.sqlRestriction("com.h3xstream.findbugs.test=1234"));

        final String localSafe = "where id=1337";

        session.createQuery("select t from UserEntity t "+localSafe);

        final String localSql = "select * from TestEntity " + localSafe;

        session.createSQLQuery(localSql);
    }
}
