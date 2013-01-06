package javax.jdo;

public interface PersistenceManager {
    Query newQuery(String s);

    Query newQuery(String sql, Object s);
}
