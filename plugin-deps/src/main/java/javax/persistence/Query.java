package javax.persistence;

public interface Query {

    Object getSingleResult();

    Query setParameter(int position, Object value);

}
