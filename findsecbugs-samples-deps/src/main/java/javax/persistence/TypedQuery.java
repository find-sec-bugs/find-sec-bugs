package javax.persistence;

//http://docs.oracle.com/javaee/6/api/javax/persistence/TypedQuery.html
public interface TypedQuery<X> extends Query {
    X getSingleResult();
    TypedQuery<X> setParameter(String name, Object value);
}
