package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.Callback;
import interfaces.Result;

public class CallbackImpl extends UnicastRemoteObject implements Callback {

    protected CallbackImpl() throws RemoteException {
        super();
    }

    @Override
    public void notify(Result result) throws RemoteException {
        System.out.println("Result received from the server: " + result);
    }

}
