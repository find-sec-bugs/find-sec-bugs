package testcode.sqli;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.google.inject.Inject;

public class JpaSql {

    @Inject
    EntityManager em;

    // The following query are subject to injection

    public void getUserByUsername(String username) {
        TypedQuery<UserEntity> q = em.createQuery(
                String.format("select * from Users where name = %s", username),
                UserEntity.class);

        UserEntity res = q.getSingleResult();
    }

    public void getUserByUsernameAlt2(String username) {
        TypedQuery<UserEntity> q = em.createQuery(
                "select * from Users where name = '" + username + "'",
                UserEntity.class);

        UserEntity res = q.getSingleResult();
    }

    // The following should be safe

    public UserEntity getFirst() {
        TypedQuery<UserEntity> q = em.createQuery("select * from Users",
                UserEntity.class);
        return q.getSingleResult();
    }

    public UserEntity getFirstAlt2() {
        final String sql = "select * from Users";
        TypedQuery<UserEntity> q = (TypedQuery<UserEntity>) em.createQuery(sql);
        return q.getSingleResult();
    }

    /**
     * Uses a StringBuilder to build sql and append constant string.
     */
    public UserEntity getUserFromSqlBuiltFromStringBuilder() {
        StringBuilder sql = new StringBuilder("select * from Users");
        sql.append(" where test is not null");
        TypedQuery<UserEntity> q = (TypedQuery<UserEntity>) em.createQuery(sql
                .toString());
        return q.getSingleResult();
    }

    /**
     * Uses a parameterized query with named parameter.
     */
    public UserEntity getUserWithNamedParameter(String userName) {
        final String sql = "select * from Users where name = :name";
        TypedQuery<UserEntity> q = em.createQuery(sql, UserEntity.class);
        return q.setParameter("name", userName).getSingleResult();
    }

    public UserEntity getUserWithNamedParameterAlt(String userName) {
        final String sql = "select * from Users where name = :name";
        TypedQuery<UserEntity> q = (TypedQuery<UserEntity>) em.createQuery(sql);
        return q.setParameter("name", userName).getSingleResult();
    }

    /**
     * Uses a parameterized query with named parameter.
     */
    public UserEntity getUserWithSqlBuildFromStringBuilderAndNamedParameter(
            String userName) {
        StringBuilder sql = new StringBuilder("select * from Users");
        sql.append("where name = :name");

        TypedQuery<UserEntity> q = (TypedQuery<UserEntity>) em.createQuery(sql
                .toString());
        return q.setParameter("name", userName).getSingleResult();
    }

    /**
     * Uses a parameterized query with indexed parameter.
     */
    public UserEntity getUserWithIndexedParameter(String userName) {
        StringBuilder sql = new StringBuilder("select * from Users");
        sql.append("where name = ?");

        TypedQuery<UserEntity> q = (TypedQuery<UserEntity>) em.createQuery(sql
                .toString());
        return q.setParameter(1, userName).getSingleResult();
    }

    public UserEntity getUserWithIndexedParameterAlt(String userName) {
        StringBuilder sql = new StringBuilder("select * from Users");
        sql.append("where name = ?1");

        TypedQuery<UserEntity> q = (TypedQuery<UserEntity>) em.createQuery(sql
                .toString());
        return q.setParameter(1, userName).getSingleResult();
    }

    /**
     * Uses a native query.
     */
    public UserEntity getUserWithNativeQuery() {
        final String sql = "select * from Users";
        Query q = em.createNativeQuery(sql);
        return (UserEntity) q.getSingleResult();
    }

    public UserEntity getUserWithNativeQueryAlt() {
        final String sql = "select * from Users";
        Query q = em.createNativeQuery(sql, UserEntity.class);
        return (UserEntity) q.getSingleResult();
    }

    public UserEntity getUserWithNativeQueryAlt2() {
        final String sql = "select * from Users";
        Query q = em.createNativeQuery(sql, "resultsetMapping");
        return (UserEntity) q.getSingleResult();
    }

    /**
     * Uses native query and parameter.
     */
    public UserEntity getUserWithNativeQueryAndIndexedParameter(String userName) {
        final String sql = "select * from Users where name = ?";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, userName);
        return (UserEntity) q.getSingleResult();
    }

    public UserEntity getUserWithNativeQueryAndIndexedParameterAlt(
            String userName) {
        final String sql = "select * from Users where name = ?1";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, userName);
        return (UserEntity) q.getSingleResult();
    }
}
