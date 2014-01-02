package javax.persistence;


public interface EntityManager {
    <T> TypedQuery<T> createQuery(String query, Class<T> type);

    Query createQuery(String sql);

    Query createNativeQuery(String paramString);

    Query createNativeQuery(String paramString, Class paramClass);
}
