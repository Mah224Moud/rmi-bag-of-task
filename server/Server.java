package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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

                taskRepository.saveResult(task.getClass().getSimpleName(), ((FibonacciTask) task).getParam(),
                        result.getResult());

                callback.notify(result);
                System.out.println("Worker " + Thread.currentThread().getId() + " finished processing a task");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        workerPool.shutdown();
    }
}
