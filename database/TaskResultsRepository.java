package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskResultsRepository {

    /**
     * Save a result in the database.
     * 
     * @param taskName    the name of the task
     * @param param       the parameter used for the task
     * @param description a short description of the task
     * @return the ID of the result in the database, or -1 if the operation failed.
     */
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

    /**
     * Save a Fibonacci sequence in the database.
     * 
     * @param taskId   the ID of the task in the database
     * @param sequence the Fibonacci sequence to be saved
     */
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

    /**
     * Checks if a parameter is already in the database.
     * 
     * @param param the parameter to check
     * @return true if the parameter exists, false otherwise
     */
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

    /**
     * Updates the result associated with the specified old parameter in the
     * database.
     * 
     * This method updates both the parameter and the Fibonacci sequence result
     * for the entry identified by the old parameter. It first retrieves the
     * task ID using the old parameter, and if found, updates the parameter
     * and the sequence in the respective tables.
     * 
     * @param oldParam   the existing parameter to identify the record to update
     * @param newParam   the new parameter value to update in the record
     * @param newResults the new Fibonacci sequence to update in the record
     */
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

    /**
     * Retrieves all parameters stored in the TASK_RESULTS table.
     *
     * @return a list of all parameters from the TASK_RESULTS table
     */
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

    /**
     * Deletes the result associated with the specified parameter in the database.
     * 
     * This method deletes both the parameter and the Fibonacci sequence result
     * for the entry identified by the parameter. It first retrieves the
     * task ID using the parameter, and if found, deletes the parameter
     * and the sequence in the respective tables.
     * 
     * @param param the parameter to identify the record to delete
     */
    public void deleteResultByParam(int param) {
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

    /**
     * Retrieves the complete task information associated with the specified
     * parameter.
     * 
     * This method retrieves both the task information and the Fibonacci sequence
     * result for the entry identified by the parameter. It first retrieves the
     * task ID using the parameter, and if found, retrieves the parameter
     * and the sequence in the respective tables.
     * 
     * @param param the parameter to identify the record to retrieve
     * @return a string containing the task information and the Fibonacci sequence
     */
    public String getCompleteTaskInfoByParam(int param) {
        String taskInfo = getResultByParam(param);
        String sequenceInfo = getFibonacciSequenceByParam(param);
        return taskInfo + " ==> La suite de Fibonacci: " + sequenceInfo;
    }

    /**
     * Retrieves the Fibonacci sequence associated with the specified parameter.
     * 
     * This method retrieves the task ID associated with the parameter and
     * then retrieves the Fibonacci sequence from the FIBONACCI_SEQUENCE
     * table for the found ID. If either the task ID or the sequence is not
     * found, the method returns a descriptive error string.
     * 
     * @param param the parameter to identify the record to retrieve
     * @return a string containing the Fibonacci sequence, or an error string
     */
    private String getFibonacciSequenceByParam(int param) {
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
        if (taskId == -1) {
            return "Aucun enregistrement trouvé pour le paramètre: " + param;
        }

        String sql = "SELECT RESULT FROM FIBONACCI_SEQUENCE WHERE ID = ?";
        try (Connection conn = DatabaseHelper.connectToButor();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("RESULT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Aucune suite Fibonacci trouvée pour le paramètre: " + param;
    }

    /**
     * Retrieves the task result associated with the specified parameter.
     * 
     * This method queries the TASK_RESULTS table to find the task details
     * for the given parameter. If a matching entry is found, it returns
     * a formatted string containing the task name, parameter, and description.
     * If no entry is found, it returns an error message.
     * 
     * @param param the parameter to identify the record to retrieve
     * @return a string containing the task information, or an error message if not
     *         found
     */
    public String getResultByParam(int param) {
        String sql = "SELECT TASK_NAME, PARAM, DESCRIPTION, DATE_CREATED FROM TASK_RESULTS WHERE PARAM = ?";
        try (Connection conn = DatabaseHelper.connectToEluard();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, param);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String taskName = rs.getString("TASK_NAME");
                int paramValue = rs.getInt("PARAM");
                String description = rs.getString("DESCRIPTION");

                return "Tâche: " + taskName + " | Paramètre: " + paramValue + " | Description: " + description;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Aucun résultat trouvé pour le paramètre: " + param;
    }

}
