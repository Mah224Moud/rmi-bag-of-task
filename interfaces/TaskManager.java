package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskManager extends Remote {
    void submitTask(Task task, Callback callback) throws RemoteException;
}
