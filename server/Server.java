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

    public void shutdown() {
        workerPool.shutdown();
        System.out.println("Server is shutting down...");
    }

    @Override
    public String getResultDetails(int id) throws RemoteException {
        return taskRepository.getCompleteTaskInfoById(id);
    }

    @Override
    public void deleteResult(int id) throws RemoteException {
        throw new UnsupportedOperationException("Unimplemented method 'deleteResult'");
    }

    @Override
    public List<Integer> listAllIds() throws RemoteException {
        return taskRepository.getAllTaskIds();
    }

    @Override
    public List<Integer> listAllParams() throws RemoteException {
        return taskRepository.getAllParams();
    }
}
