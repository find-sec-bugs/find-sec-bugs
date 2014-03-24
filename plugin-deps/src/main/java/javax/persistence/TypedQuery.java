package javax.persistence;

public interface TypedQuery<T> extends Query {
    T getSingleResult();

    TypedQuery<T> setParameter(String paramString, Object paramObject);

    TypedQuery<T> setParameter(int paramInt, Object paramObject);

}
