package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskResultsRepository {

    public void saveResult(String taskName, int param, int result) {
        String sql = "INSERT INTO TASK_RESULTS (TASK_NAME, PARAM, RESULT) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setInt(2, param);
            pstmt.setInt(3, result);
            pstmt.executeUpdate();
            System.out.println("Result saved in database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getResultById(int id) {
        String sql = "SELECT TASK_NAME, PARAM, RESULT, CREATED_AT FROM TASK_RESULTS WHERE ID = ?";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String taskName = rs.getString("TASK_NAME");
                int param = rs.getInt("PARAM");
                int result = rs.getInt("RESULT");
                String createdAt = rs.getTimestamp("CREATED_AT").toString();

                return "Task: " + taskName + " | " + param + "ème nombre de Fibonacci est " + result +
                        " | Executed on: " + createdAt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No result found for ID: " + id;
    }

    public void updateResult(int id, int newResult) {
        String sql = "UPDATE TASK_RESULTS SET RESULT = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newResult);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Result updated in database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteResult(int id) {
        String sql = "DELETE FROM TASK_RESULTS WHERE ID = ?";
        try (Connection conn = DatabaseHelper.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Result deleted from database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
