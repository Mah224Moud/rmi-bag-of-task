package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import database.TaskResultsRepository;
import interfaces.Callback;
import interfaces.Result;
import interfaces.Task;
import interfaces.TaskManager;
import tasks.FibonacciTask;
import worker.Worker;

public class Server extends UnicastRemoteObject implements TaskManager {
    private static final int NUM_WORKERS = 5;
    private final ExecutorService workerPool;
    private final TaskResultsRepository taskRepository;

    public Server() throws RemoteException {
        super();
        workerPool = Executors.newFixedThreadPool(NUM_WORKERS);
        taskRepository = new TaskResultsRepository();
    }

    /**
     * Submit a task to the server to be processed by a worker.
     * 
     * The task is submitted to a pool of workers, and the result is
     * returned to the client using the provided callback.
     * 
     * The server will prevent duplicate calculations of the same task
     * by checking if the same parameter has already been calculated in
     * the database.
     * 
     * @param task     the task to be submitted
     * @param callback the callback that will be notified when the result
     *                 is available
     * @throws RemoteException if a network error occurs
     */
    @Override
    public void submitTask(Task task, Callback callback) throws RemoteException {
        workerPool.submit(() -> {
            try {
                Worker worker = new Worker();
                System.out.println("Worker " + Thread.currentThread().getId() + " started processing a task");

                Result result = worker.executeTask(task);
                List<Integer> fibonacciSequence = result.getFibonacciSequence();

                int param = ((FibonacciTask) task).getParam();
                String description = ((FibonacciTask) task).getDescription();

                if (taskRepository.isParamExists(param)) {
                    callback.alert("Les " + param + " premiers nombres de Fibo ont déja été calculés");
                    callback.notify(result);
                } else {
                    int taskId = taskRepository.saveResult(task.getClass().getSimpleName(), param, description);

                    taskRepository.saveFibonacciSequence(taskId, fibonacciSequence);

                    callback.notify(result);
                }
                System.out.println("Le client a été notifié !!!");

                System.out.println("Worker " + Thread.currentThread().getId() + " finished processing a task\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Updates the result associated with the specified old parameter.
     * 
     * If the old parameter does not exist, notifies the client that no
     * update was made.
     * 
     * @param oldParam the existing parameter to identify the record to update
     * @param task     the task that will be used to generate the new result
     * @param callback the callback that will be notified when the result
     *                 is available
     * @throws RemoteException if a network error occurs
     */
    @Override
    public void updateResult(int oldParam, Task task, Callback callback) throws RemoteException {
        Worker worker = new Worker();
        System.out.println("Worker " + Thread.currentThread().getId() + " started processing a task");
        Result result = worker.executeTask(task);
        List<Integer> newResults = result.getFibonacciSequence();
        if (!taskRepository.isParamExists(oldParam)) {
            callback.alert("Le paramètre " + oldParam + " n'existe pas. Aucune mise à jour effectuée.");
            return;
        }
        taskRepository.updateResultByParam(oldParam, ((FibonacciTask) task).getParam(), newResults);
        callback.notify(result);

        System.out.println("Worker " + Thread.currentThread().getId() + " finished processing a task\n");
    }

    /**
     * Deletes the result associated with the specified parameter.
     * 
     * If the parameter does not exist, notifies the client that no
     * deletion was made.
     * 
     * @param param    the parameter to identify the record to delete
     * @param callback the callback that will be notified when the deletion
     *                 is complete
     * @throws RemoteException if a network error occurs
     */
    @Override
    public void deleteResult(int param, Callback callback) throws RemoteException {
        taskRepository.deleteResultByParam(param);
        callback.alert("Le résultat avec le paramètre " + param + " a été supprimé.");
    }

    /**
     * Retrieves the complete task information associated with the specified
     * parameter.
     * 
     * If the parameter does not exist, notifies the client that no result
     * was found.
     * 
     * @param param    the parameter to identify the record to retrieve
     * @param callback the callback that will be notified with the result
     * @throws RemoteException if a network error occurs
     */
    @Override
    public void getResultByParam(int param, Callback callback) throws RemoteException {
        if (!taskRepository.isParamExists(param)) {
            callback.alert("Le paramètre " + param + " n'existe pas.");
            return;
        }

        String completeInfo = taskRepository.getCompleteTaskInfoByParam(param);
        callback.alert(completeInfo);
        System.out.println("Le client a été notifié !!!");
    }

    /**
     * Retrieves all parameters stored in the database.
     * 
     * @return a list of all parameters from the database
     * @throws RemoteException if a network error occurs
     */
    @Override
    public List<Integer> listAllParams() throws RemoteException {
        return taskRepository.getAllParams();
    }

    /**
     * Shuts down the server gracefully by terminating the worker pool.
     * 
     * This method ensures that all running tasks are completed before
     * shutting down the server. It also logs a message indicating that
     * the server is shutting down.
     */
    public void shutdown() {
        workerPool.shutdown();
        System.out.println("Server is shutting down...");
    }

}
