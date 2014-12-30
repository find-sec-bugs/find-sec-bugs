package testcode.sqli;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

public class HibernateSql {

    public void testQueries(SessionFactory sessionFactory, String input) {

        Session session = sessionFactory.openSession();

        Criteria criteria = session.createCriteria(UserEntity.class);

        //The following would need to be audited

        criteria.add(Restrictions.sqlRestriction("test=1234" + input));

        session.createQuery("select t from UserEntity t where id = " + input);

        session.createSQLQuery(String.format("select * from TestEntity where id = %s ", input));

        //More sqlRestriction signatures

        criteria.add(Restrictions.sqlRestriction("param1  = ? and param2 = " + input,input, StandardBasicTypes.STRING));
        criteria.add(Restrictions.sqlRestriction("param1  = ? and param2 = " + input,new String[] {input}, new Type[] {StandardBasicTypes.STRING}));

        //OK nothing risky here..

        criteria.add(Restrictions.sqlRestriction("test=1234"));

        final String localSafe = "where id=1337";

        session.createQuery("select t from UserEntity t " + localSafe);

        final String localSql = "select * from TestEntity " + localSafe;

        session.createSQLQuery(localSql);

        //More sqlRestriction signatures (with safe binding)

        criteria.add(Restrictions.sqlRestriction("param1  = ?",input, StandardBasicTypes.STRING));
        criteria.add(Restrictions.sqlRestriction("param1  = ? and param2 = ?", new String[] {input}, new Type[] {StandardBasicTypes.STRING}));

    }
}
