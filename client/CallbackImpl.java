package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.Callback;
import interfaces.Result;

public class CallbackImpl extends UnicastRemoteObject implements Callback {

    protected CallbackImpl() throws RemoteException {
        super();
    }

    /**
     * Notify the client that the result of a task is available.
     * 
     * @param result the result of the task
     * @throws RemoteException if a network error occurs
     */
    @Override
    public void notify(Result result) throws RemoteException {
        System.out.println("Result received from the server: " + result);
    }

    /**
     * Display a message to the user.
     * 
     * @param msg the message to display
     * @throws RemoteException if a network error occurs
     */
    @Override
    public void alert(String msg) throws RemoteException {
        System.out.println(msg);
    }

}
