package javax.persistence;

public interface TypedQuery<T> extends Query {
    T getSingleResult();
}
