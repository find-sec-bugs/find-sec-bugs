package testcode.sqli.stringbuilder;

import testcode.sqli.UserEntity;

import javax.persistence.EntityManager;

public class StringBuilderFalsePositive {

    EntityManager em;

    public void queryConditionalConcat(String username,boolean onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");
        if(onlyActive) {
            sql.append(" and active = true");
        }

        UserEntity res = em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public void queryNoAppend(String username,boolean onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");

        UserEntity res = em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public void queryNoConstructor(String username,boolean onlyActive) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from Users where name = usernameParam");

        UserEntity res = em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

    }

    public void queryUnrelatedStringBuilder(String username,boolean onlyActive) {
        StringBuilder sql = new StringBuilder("select * from Users where name = usernameParam");

        UserEntity res = em.createQuery(sql.toString(), UserEntity.class)
                .setParameter("usernameParam", username)
                .getSingleResult();

        new StringBuilder("this is ").append(username).append(" totally unrelated.").toString();
    }
}
