package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DatabaseHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelperTest {
    public static void main(String[] args) {
        testEluardConnection();
        testButorConnection();
    }

    public static void testEluardConnection() {
        try (Connection conn = DatabaseHelper.connectToEluard()) {
            System.out.println("Connexion réussie à la base de données eluard !");
            // Effectuer une requête simple
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM TASK_RESULTS"); // Assure-toi que la table existe

            if (rs.next()) {
                System.out.println("Nombre de résultats dans TASK_RESULTS : " + rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à eluard : " + e.getMessage());
        }
    }

    public static void testButorConnection() {
        try (Connection conn = DatabaseHelper.connectToButor()) {
            System.out.println("Connexion réussie à la base de données butor !");
            // Effectuer une requête simple
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM FIBONACCI_SEQUENCE"); // Assure-toi que la table
                                                                                         // existe

            if (rs.next()) {
                System.out.println("Nombre de résultats dans FIBONACCI_SEQUENCE : " + rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à butor : " + e.getMessage());
        }
    }
}
