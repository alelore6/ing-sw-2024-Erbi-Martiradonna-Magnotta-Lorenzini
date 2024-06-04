package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    /**
     * binds a client to the server
     * @throws RemoteException
     */
    void register(Client client) throws RemoteException;

    void update(Client client, GenericEvent event) throws RemoteException;

}
