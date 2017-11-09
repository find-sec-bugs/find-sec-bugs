package testcode.crypto;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ErrorMessages {
    JdbcTemplate jdbcTemplate;

    public void vulnerableErrorMessage1() {
        try {
            String password = "Password";
            Connection conn = DriverManager.getConnection("jdbc:mysql://prod.company.com/production");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void vulnerableErrorMessage2() {
        FileInputStream fis = null;
        try {
            String fileName = "fileName";
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
