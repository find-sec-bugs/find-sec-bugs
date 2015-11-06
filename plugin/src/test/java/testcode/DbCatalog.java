package testcode;

import java.sql.Connection;
import java.sql.SQLException;

public class DbCatalog {
    public void callSetCatalog(Connection c) throws SQLException {
        String tainted = System.getProperty("");
        c.setCatalog("safe"); // ok
        c.setCatalog(tainted);
        c.setCatalog("very ".concat("safe").toUpperCase()); // ok
    }
}
