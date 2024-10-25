package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.Task;
import interfaces.TaskManager;
import tasks.FibonacciTask;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            TaskManager taskManager = (TaskManager) registry.lookup("TaskManager");

            Task task = new FibonacciTask(9);
            taskManager.submitTask(task, new CallbackImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
