package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.Task;
import interfaces.TaskManager;
import tasks.FibonacciTask;
import java.util.List;
import java.util.Scanner;

public class Client {

    private static TaskManager server;
    private static Scanner scanner;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            server = (TaskManager) registry.lookup("TaskManager");

            scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                running = handleChoice(choice);
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the main menu of the client and waits for user input.
     */
    private static void displayMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1 - Créer une tâche");
        System.out.println("2 - Consulter un résultat");
        System.out.println("3 - Mettre à jour un résultat");
        System.out.println("4 - Supprimer un résultat");
        System.out.println("5 - Quitter");
        System.out.print("Choisissez une option : ");
    }

    /**
     * Handles the user's menu choice.
     *
     * @param choice the user's menu choice
     * @return true if the client should continue running, false if the client
     *         should exit
     */
    private static boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                createTask();
                break;
            case 2:
                consultResult();
                break;
            case 3:
                updateResult();
                break;
            case 4:
                deleteResult();
                break;
            case 5:
                System.out.println("Fermeture du client.");
                System.exit(0);
                return false;
            default:
                System.out.println("Option invalide. Veuillez réessayer.");
        }
        return true;
    }

    /**
     * Asks the user to enter a number and creates a FibonacciTask with
     * this number. The task is then submitted to the server with a
     * CallbackImpl as the callback.
     */
    private static void createTask() {
        System.out.print("Entrez un nombre pour Fibonacci: ");
        int n = scanner.nextInt();
        Task task = new FibonacciTask(n);
        try {
            server.submitTask(task, new CallbackImpl());
            System.out.println("Tâche soumise pour calcul de Fibonacci(" + n + ")");
        } catch (Exception e) {
            System.out.println("Erreur lors de la soumission de la tâche.");
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to enter a parameter to consult a result.
     * Lists all the calculated Fibo parameters and checks if the entered
     * parameter exists. If it exists, requests the server to provide
     * the result associated with the parameter using a callback.
     * If the parameter does not exist, notifies the user.
     */
    private static void consultResult() {
        try {
            List<Integer> params = server.listAllParams();
            System.out.println("Paramètres calculés : " + params);
            System.out.print("Entrez le paramètre du résultat à consulter : ");
            int param = scanner.nextInt();

            if (!params.contains(param)) {
                System.out.println("Le paramètre " + param + " n'existe pas.");
                return;
            }

            server.getResultByParam(param, new CallbackImpl());
        } catch (Exception e) {
            System.out.println("Erreur lors de la consultation du résultat.");
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to enter a parameter to update a result.
     * Lists all the calculated Fibo parameters and checks if the entered
     * parameter exists. If it exists, requests the server to provide
     * the result associated with the parameter using a callback.
     * If the parameter does not exist, notifies the user.
     * If the update is successful, notifies the user.
     */
    private static void updateResult() {
        try {
            List<Integer> params = server.listAllParams();
            System.out.println("Paramètres calculés : " + params);

            System.out.print("Entrez le paramètre à mettre à jour : ");
            int oldParam = scanner.nextInt();

            if (!params.contains(oldParam)) {
                System.out.println("Le paramètre " + oldParam + " n'existe pas.");
                return;
            }

            System.out.print("Entrez le nouveau paramètre : ");
            int newParam = scanner.nextInt();
            Task task = new FibonacciTask(newParam);
            server.updateResult(oldParam, task, new CallbackImpl());
            System.out.println("Tâche soumise pour mise à jour de Fibonacci avec le nouveau paramètre : " + newParam);

        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour du résultat.");
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to enter a parameter to delete a result.
     * Lists all the calculated Fibo parameters and checks if the entered
     * parameter exists. If it exists, requests the server to delete
     * the result associated with the parameter using a callback.
     * If the parameter does not exist, notifies the user.
     * If the deletion is successful, notifies the user.
     */
    private static void deleteResult() {
        try {
            List<Integer> params = server.listAllParams();
            System.out.println("Paramètres calculés : " + params);

            System.out.print("Entrez le paramètre à supprimer : ");
            int param = scanner.nextInt();

            if (!params.contains(param)) {
                System.out.println("Le paramètre " + param + " n'existe pas.");
                return;
            }

            server.deleteResult(param, new CallbackImpl());
            System.out.println("Tâche soumise pour suppression du paramètre: " + param);
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression du résultat.");
            e.printStackTrace();
        }
    }

}
