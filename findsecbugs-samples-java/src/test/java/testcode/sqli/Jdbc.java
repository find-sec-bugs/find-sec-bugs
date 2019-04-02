package testcode.sqli;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Jdbc {

    Connection con;

    public void query1(String input) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from Users where name = '"+input+"'");
    }

    public void query2(String input) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "select * from Users where name = '" + input + "'";
        ResultSet rs = stmt.executeQuery(sql);
    }

    public void query3(String input) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format("select * from Users where name = '%s'",input));
    }

    public void query4(String input) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "select * from Users where name = '%s'";
        ResultSet rs = stmt.executeQuery(String.format(sql,input));
    }

    public void executeQuerySamples(String sql) throws SQLException {

        Statement stmt = con.createStatement();

        //Normal query
        stmt.executeQuery(sql);

        stmt.execute(sql);
        stmt.execute(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.execute(sql, new int[]{1, 2, 3});
        stmt.execute(sql, new String[]{"firstname", "middlename", "lastname"});
    }

    public void executeUpdateSamples(String sql) throws SQLException {

        Statement stmt = con.createStatement();

        //Update query
        stmt.executeUpdate(sql);
        stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate(sql, new int[]{1, 2, 3});
        stmt.executeUpdate(sql, new String[]{"firstname", "middlename", "lastname"});
    }


    public void executeExecuteLargeUpdateSamples(String sql) throws SQLException {

        Statement stmt = con.createStatement();

        stmt.executeLargeUpdate(sql);
        stmt.executeLargeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.executeLargeUpdate(sql, new int[]{1, 2, 3});
        stmt.executeLargeUpdate(sql, new String[]{"firstname", "middlename", "lastname"});
    }


    public void executePrepareCallSamples(String sql) throws SQLException {
        //Prepare Call
        con.prepareCall(sql);
        con.prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        con.prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }


    public void prepareStatementSamples(String sql) throws SQLException {
        //Prepare Statement
        con.prepareStatement(sql);
        con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
        con.prepareStatement(sql, new int[]{1, 2, 3});
        con.prepareStatement(sql, new String[]{"firstname", "middlename", "lastname"});

    }

    public void otherSamples(String sql) throws SQLException {
        //Native
        con.nativeSQL(sql);

        Statement stmt = con.createStatement();
        stmt.addBatch(sql);
    }

}
