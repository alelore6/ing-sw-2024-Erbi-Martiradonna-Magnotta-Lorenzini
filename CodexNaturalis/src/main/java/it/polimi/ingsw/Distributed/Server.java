package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    /**
     * binds a client to the server
     * @throws RemoteException
     */

    //process client
    void register(Client client) throws RemoteException;

    //process event
    void update(Client client, GenericEvent event) throws RemoteException;

}
