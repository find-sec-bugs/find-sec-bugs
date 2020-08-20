package testcode.bugs;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WebGoatCreateDb529 {

    private void createTransactionTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        try {
            String dropTable = "DROP TABLE transactions";
            statement.executeUpdate(dropTable);
        } catch (SQLException e) {
            System.out.println("Info - Could not drop transactions table");
        }

        try {
            String createTable = "CREATE TABLE Transactions (" + "userName VARCHAR(16) NOT NULL, "
                    + "sequence INTEGER NOT NULL, " + "from_account VARCHAR(16) NOT NULL, "
                    + "to_account VARCHAR(16) NOT NULL, " + "transactionDate TIMESTAMP NOT NULL, "
                    + "description VARCHAR(255) NOT NULL, " + "amount INTEGER NOT NULL" + ")";

            statement.executeUpdate(createTable);
        } catch (SQLException e) {
            System.out.println("Error: unable to create transactions table: " + e.getLocalizedMessage());
            throw e;
        }

        String[] data = new String[]{
                "'dave', 0, '238-4723-4024', '324-7635-9867', '2008-02-06 21:40:00', 'Mortgage', '150'",
                "'dave', 1, '238-4723-4024', '324-7635-9867', '2008-02-12 21:41:00', 'Car', '150'",
                "'dave', 2, '238-4723-4024', '324-7635-9867', '2008-02-20 21:42:00', 'School fees', '150'",
                "'CEO', 3, '348-6324-9872', '345-3490-8345', '2008-02-15 21:40:00', 'Rolls Royce', '-150000'",
                "'CEO', 4, '348-6324-9872', '342-5893-4503', '2008-02-25 21:41:00', 'Mansion', '-150000'",
                "'CEO', 5, '348-6324-9872', '980-2344-5492', '2008-02-27 21:42:00', 'Vacation', '-150000'",
                "'jeff', 6, '934-2002-3485', '783-2409-8234', '2008-02-01 21:40:00', 'Vet', '250'",
                "'jeff', 7, '934-2002-3485', '634-5879-0345', '2008-02-19 21:41:00', 'Doctor', '800'",
                "'jeff', 8, '934-2002-3485', '435-4325-3358', '2008-02-20 21:42:00', 'X-rays', '200'",};
        try {
            for (int i = 0; i < data.length; i++) {
                statement.executeUpdate("INSERT INTO Transactions VALUES (" + data[i] + ");");
            }
        } catch (SQLException sqle) {
            System.out.println("Error: Unable to insert transactions:  " + sqle.getLocalizedMessage());
            int errorCode = sqle.getErrorCode();
            System.out.println("Error Code: " + errorCode);
            // ignore exceptions for Oracle and SQL Server
            if (errorCode != 911 && errorCode != 273) {
                throw sqle;
            }
        }
    }
}
