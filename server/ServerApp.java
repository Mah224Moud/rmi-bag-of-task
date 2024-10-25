package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            Server server = new Server();
            registry.rebind("TaskManager", server);

            System.out.println("Server is running...");

            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
