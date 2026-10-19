package jakarta.persistence;

public interface EntityManager {

    <T> TypedQuery<T> createQuery(String query, Class<T> type);
    Query createQuery(String sql);

    Query createNativeQuery(String sqlString);
    Query createNativeQuery(String sqlString, String resultClass);
    Query createNativeQuery(String sqlString, Class resultClass);
}
