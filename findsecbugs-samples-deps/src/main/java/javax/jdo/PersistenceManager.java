package javax.jdo;

import java.util.Collection;

public interface PersistenceManager {
    Query newQuery(String s);

    Query newQuery(String sql, Object s);

    Query newQuery(Class var1);

    Query newQuery(Class var1,Collection collection,String filter);

    Query newQuery(Class var1,String filter);

    Query newQuery(Extent var1,String filter);
}
