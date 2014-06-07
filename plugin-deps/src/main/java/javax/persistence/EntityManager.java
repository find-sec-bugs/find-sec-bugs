package javax.persistence;

import javax.persistence.criteria.CriteriaQuery;

/**
 * Ref: http://docs.oracle.com/javaee/6/api/javax/persistence/EntityManager.html
 */
public interface EntityManager {

    <T> TypedQuery<T> createQuery(String query, Class<T> type);
    Query createQuery(String sql);

    Query createNativeQuery(String sqlString);
    Query createNativeQuery(String sqlString, String resultClass);
    Query createNativeQuery(String sqlString, Class resultClass);
}
