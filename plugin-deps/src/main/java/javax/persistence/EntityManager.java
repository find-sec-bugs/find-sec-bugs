package javax.persistence;

import javax.persistence.criteria.CriteriaQuery;

public interface EntityManager {
    <T> TypedQuery<T> createQuery(String query,Class<T> type);

    Query createQuery(String sql);
}
