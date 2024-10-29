package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String ELUARD_DB_URL = "jdbc:mysql://localhost:3306/eluard";
    private static final String BUTOR_DB_URL = "jdbc:mysql://localhost:3306/butor";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Moud1997@";

    public static Connection connectToEluard() throws SQLException {
        return DriverManager.getConnection(ELUARD_DB_URL, DB_USER, DB_PASSWORD);
    }

    public static Connection connectToButor() throws SQLException {
        return DriverManager.getConnection(BUTOR_DB_URL, DB_USER, DB_PASSWORD);
    }
}
