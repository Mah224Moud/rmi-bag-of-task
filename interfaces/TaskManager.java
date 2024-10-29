package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaskManager extends Remote {
    void submitTask(Task task, Callback callback) throws RemoteException;

    String getResultDetails(int id) throws RemoteException;

    void updateResult(int id, int newResult) throws RemoteException;

    void deleteResult(int id) throws RemoteException;

    List<Integer> listAllIds() throws RemoteException;
}
