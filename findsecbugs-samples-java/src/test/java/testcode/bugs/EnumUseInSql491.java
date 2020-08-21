package testcode.bugs;

import testcode.sqli.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EnumUseInSql491 {
    EntityManager entityManager;

    private String prepareQuery(SomeEnum someEnum) {
        String sql = " ... some SQL... " + someEnum.name(); // This line causes the problem.
        return sql;
    }

    public void doSQL(SomeEnum value){
        Query q = entityManager.createNativeQuery(prepareQuery(value), UserEntity.class);
    }

    private String prepareQuery2(Enum someEnum) {
        String sql = " ... some SQL... " + someEnum.toString(); // This line causes the problem.
        return sql;
    }

    public void doSQL2(SomeEnum value){
        Query q = entityManager.createNativeQuery(prepareQuery2(value), UserEntity.class);
    }
}
