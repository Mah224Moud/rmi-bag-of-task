package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:oracle:thin:@eluard:1521:ENSE2024";
    private static final String DB_USER = "md994144";
    private static final String DB_PASSWORD = "md994144";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
