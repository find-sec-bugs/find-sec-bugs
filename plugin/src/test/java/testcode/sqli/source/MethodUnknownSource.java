package testcode.sqli.source;

import testcode.sqli.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public abstract class MethodUnknownSource {

    EntityManager em;

    public void getUserByUsername1() {
        String username = unknownSource();

        TypedQuery<UserEntity> q = em.createQuery(
                String.format("select * from Users where name = %s", username),
                UserEntity.class);

        UserEntity res = q.getSingleResult();
    }

    public void getUserByUsername2() {
        String username = unknownEncoder(unknownSource());

        TypedQuery<UserEntity> q = em.createQuery(
                "select * from Users where name = '" + username + "'",
                UserEntity.class);

        UserEntity res = q.getSingleResult();
    }

    public abstract String unknownSource();
    public abstract String unknownEncoder(String value);
}
