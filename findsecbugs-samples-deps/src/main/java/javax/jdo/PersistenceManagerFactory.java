package javax.jdo;

import java.io.Serializable;

public interface PersistenceManagerFactory extends Serializable {
    PersistenceManager getPersistenceManager();
}
