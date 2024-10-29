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
                System.out.println("Le resulat a été enregistré dans eluard pour l'id:: " + taskId);
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
            System.out.println("La suite de Fibonacci a été enregistré dans butor pour l'id: " + taskId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getResultById(int id) {
        String sql = "SELECT TASK_NAME, PARAM, DESCRIPTION, DATE_CREATED FROM TASK_RESULTS WHERE ID = ?";
        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String taskName = rs.getString("TASK_NAME");
                int param = rs.getInt("PARAM");
                String description = rs.getString("DESCRIPTION");

                return "Tâche: " + taskName + " | Paramètre: " + param + " | Description: " + description;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Aucun résultat trouvé pour l'id: " + id;
    }

    private String getFibonacciSequenceById(int id) {
        String sql = "SELECT RESULT FROM FIBONACCI_SEQUENCE WHERE ID = ?";
        try (Connection conn = DatabaseHelper.connectToButor();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("RESULT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Aucune suite Fibonacci trouvée pour l'id: " + id;
    }

    public String getCompleteTaskInfoById(int id) {
        String taskInfo = getResultById(id);
        String sequenceInfo = getFibonacciSequenceById(id);
        return taskInfo + " ==> La suite de Fibonacci: " + sequenceInfo;
    }

    public List<Integer> getAllTaskIds() {
        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT ID FROM TASK_RESULTS";
        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }

    public boolean isParamExists(int param) {
        String sql = "SELECT COUNT(*) FROM TASK_RESULTS WHERE PARAM = ?";
        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, param);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateResultByParam(int oldParam, int newParam, List<Integer> newResults) {
        String sqlGetId = "SELECT ID FROM TASK_RESULTS WHERE PARAM = ?";
        int taskId = -1;

        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sqlGetId)) {
            pstmt.setInt(1, oldParam);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (taskId != -1) {
            String sqlUpdateEluard = "UPDATE TASK_RESULTS SET PARAM = ? WHERE ID = ?";
            try (Connection conn = DatabaseHelper.connectToEluard();
                    PreparedStatement pstmt = conn.prepareStatement(sqlUpdateEluard)) {
                pstmt.setInt(1, newParam);
                pstmt.setInt(2, taskId);
                pstmt.executeUpdate();
                System.out.println("Le paramètre a été mis à jour dans eluard pour ID: " + taskId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String sequenceString = newResults.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            String sqlUpdateButor = "UPDATE FIBONACCI_SEQUENCE SET RESULT = ? WHERE ID = ?";
            try (Connection conn = DatabaseHelper.connectToButor();
                    PreparedStatement pstmt = conn.prepareStatement(sqlUpdateButor)) {
                pstmt.setString(1, sequenceString);
                pstmt.setInt(2, taskId);
                pstmt.executeUpdate();
                System.out.println("La suite de Fibonacci a été mise à jour dans butor pour ID: " + taskId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun enregistrement trouvé avec le paramètre: " + oldParam);
        }
    }

    public int getTaskIdByParam(int param) {
        String sql = "SELECT ID FROM TASK_RESULTS WHERE PARAM = ?";
        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, param);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Integer> getAllParams() {
        List<Integer> params = new ArrayList<>();
        String sql = "SELECT PARAM FROM TASK_RESULTS";

        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                params.add(rs.getInt("PARAM"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return params;
    }

    public void deleteResultByParam(int param) {
        // Récupérer l'ID du paramètre à supprimer
        String sqlGetId = "SELECT ID FROM TASK_RESULTS WHERE PARAM = ?";
        int taskId = -1;

        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sqlGetId)) {
            pstmt.setInt(1, param);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                taskId = rs.getInt("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Supprimer le résultat dans butor
        if (taskId != -1) {
            String sqlDeleteButor = "DELETE FROM FIBONACCI_SEQUENCE WHERE ID = ?";
            try (Connection conn = DatabaseHelper.connectToButor();
                    PreparedStatement pstmt = conn.prepareStatement(sqlDeleteButor)) {
                pstmt.setInt(1, taskId);
                pstmt.executeUpdate();
                System.out.println("La suite de Fibonacci a été supprimée de butor pour ID: " + taskId);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Supprimer le résultat dans eluard
            String sqlDeleteEluard = "DELETE FROM TASK_RESULTS WHERE ID = ?";
            try (Connection conn = DatabaseHelper.connectToEluard();
                    PreparedStatement pstmt = conn.prepareStatement(sqlDeleteEluard)) {
                pstmt.setInt(1, taskId);
                pstmt.executeUpdate();
                System.out.println("Le résultat a été supprimé d'eluard pour ID: " + taskId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun enregistrement trouvé avec le paramètre: " + param);
        }
    }

}
