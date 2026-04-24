package jakarta.persistence;

public interface TypedQuery<X> extends Query {
    X getSingleResult();
    TypedQuery<X> setParameter(String name, Object value);
}
