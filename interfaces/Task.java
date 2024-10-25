package interfaces;

import java.rmi.RemoteException;
import java.io.Serializable;
import java.rmi.Remote;

public interface Task extends Remote, Serializable {
    Result execute() throws RemoteException;
}
