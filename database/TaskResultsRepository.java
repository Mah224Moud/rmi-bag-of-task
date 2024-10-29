package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskResultsRepository {

    public int saveResult(String taskName, int param, String description) {
        String sql = "INSERT INTO TASK_RESULTS (TASK_NAME, PARAM, DESCRIPTION, DATE_CREATED) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "ID" })) {
            pstmt.setString(1, taskName);
            pstmt.setInt(2, param);
            pstmt.setString(3, description);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int taskId = rs.getInt(1);
                System.out.println("Result saved in eluard with taskId: " + taskId);
                return taskId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveFibonacciSequence(int taskId, List<Integer> sequence) {
        String sequenceString = sequence.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String sql = "INSERT INTO FIBONACCI_SEQUENCE (ID, RESULT) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.connectToButor(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.setString(2, sequenceString);
            pstmt.executeUpdate();
            System.out.println("Fibonacci sequence saved in butor for taskId: " + taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
