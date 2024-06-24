package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that describes the server structure. Classes that implement it can be viewed as servers.
 */
public interface Server extends Remote {

    /**
     * Method to pong with clients.
     * @throws RemoteException
     */
    void ping() throws RemoteException;

    /**
     * Method to update servers with an event.
     * @param client
     * @param event
     * @throws RemoteException
     */
    void update(Client client, GenericEvent event) throws RemoteException;

}
