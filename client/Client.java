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

    private static void displayMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1 - Créer une tâche");
        System.out.println("2 - Consulter un résultat");
        System.out.println("3 - Mettre à jour un résultat");
        System.out.println("4 - Supprimer un résultat");
        System.out.println("5 - Quitter");
        System.out.print("Choisissez une option : ");
    }

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
                return false;
            default:
                System.out.println("Option invalide. Veuillez réessayer.");
        }
        return true;
    }

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

    private static void consultResult() {
        try {
            List<Integer> ids = server.listAllIds();
            System.out.println("IDs disponibles : " + ids);
            System.out.print("Entrez l'ID du résultat à consulter : ");
            int id = scanner.nextInt();
            String resultDetails = server.getResultDetails(id);
            System.out.println(resultDetails);
        } catch (Exception e) {
            System.out.println("Erreur lors de la consultation du résultat.");
            e.printStackTrace();
        }
    }

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
