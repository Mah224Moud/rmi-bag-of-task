package worker;

import java.rmi.RemoteException;

import interfaces.Result;
import interfaces.Task;

public class Worker {
    /**
     * Executes the given task and returns the result.
     * 
     * @param task the task to execute
     * @return the result of the task
     * @throws RemoteException if a network error occurs
     */
    public Result executeTask(Task task) throws RemoteException {
        return task.execute();
    }
}
