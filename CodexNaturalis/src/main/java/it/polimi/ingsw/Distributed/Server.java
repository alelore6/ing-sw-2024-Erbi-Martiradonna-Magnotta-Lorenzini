package it.polimi.ingsw.Distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    /**
     * binds a client to the server
     * @param client the client to bind
     * @throws RemoteException
     */
    boolean addClient(Client client) throws RemoteException;

}
