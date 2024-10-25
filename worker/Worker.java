package worker;

import java.rmi.RemoteException;

import interfaces.Result;
import interfaces.Task;

public class Worker {
    public Result executeTask(Task task) throws RemoteException {
        return task.execute();
    }
}
