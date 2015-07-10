package testcode.sqli.stringbuilder;

import testcode.sqli.UserEntity;

import javax.persistence.EntityManager;

public abstract class StringBuilderSuspicious {

    EntityManager em;

    public UserEntity queryTaintedValueInConstructor(String username,boolean onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = '"+username+"'");
        if(onlyActive) {
            sql.append(" and active = true");
        }

        return em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public UserEntity queryTaintedValueInAppendMethod1(String username,String onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");
        if(!"".equals(onlyActive)) {
            sql.append(" and active = "+onlyActive);
        }

        return em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public UserEntity queryTaintedValueInAppendMethod2(String username,String onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");
        sql.append(" ");
        sql.append(" and active = "+onlyActive);
        sql.append(" ");

        return em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public abstract StringBuilder getSomeStringBuilder();

    public UserEntity queryUnknownSource1(String username,String onlyActive) {
        StringBuilder sql = getSomeStringBuilder(); //Unknown
        sql.append(" and active = true");
        sql.append(" and super = true");
        sql.append(" and magic = true");

        return em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public abstract String getSomeExtraCondition(String username);

    public UserEntity queryUnknownSource2(String username,String onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");
        sql.append(" and active = true");
        sql.append(getSomeExtraCondition(username)); //Unknown
        sql.append(" and magic = true");

        return em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public abstract void modifyMe(StringBuilder buffer);

    // I think this should be reported with low priority only, everything visible is constant
    public UserEntity queryUnknownTransformation(String username,String onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");
        sql.append(" and active = true");
        modifyMe(sql); //Unknown

        return em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

}
