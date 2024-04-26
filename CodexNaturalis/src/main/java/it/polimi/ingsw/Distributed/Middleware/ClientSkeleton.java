package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Distributed.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSkeleton implements Client{

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientSkeleton(Socket socket)throws RemoteException {
        //TODO implement
    }

    @Override
    public void update(PreviousView v, Event e) throws RemoteException {

    }
}
