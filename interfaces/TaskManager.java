package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TaskManager extends Remote {
    void submitTask(Task task, Callback callback) throws RemoteException;

    void getResultByParam(int param, Callback callback) throws RemoteException;

    void updateResult(int oldParam, Task task, Callback callback) throws RemoteException;

    void deleteResult(int param, Callback callback) throws RemoteException;

    List<Integer> listAllParams() throws RemoteException;
}
