package testcode;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.SQLException;

public class DbCatalog {
    public void callSetCatalog(Connection c,HttpServletRequest req) throws SQLException {
        String tainted = req.getParameter("input");
        c.setCatalog("safe"); // ok
        c.setCatalog(tainted);
        c.setCatalog("very ".concat("safe").toUpperCase()); // ok
    }
}
