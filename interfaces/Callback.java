package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callback extends Remote {
    void notify(Result result) throws RemoteException;

    void alert(String msg) throws RemoteException;
}
