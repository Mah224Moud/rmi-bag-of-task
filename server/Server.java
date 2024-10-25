package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.Callback;
import interfaces.Result;
import interfaces.Task;
import interfaces.TaskManager;
import worker.Worker;

public class Server extends UnicastRemoteObject implements TaskManager {
    private Worker worker = new Worker();

    public Server() throws RemoteException {
        super();
    }

    @Override
    public void submitTask(Task task, Callback callback) throws RemoteException {
        Result result = worker.executeTask(task);
        callback.notify(result);
    }
}
