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
     * @throws RemoteException default rmi exception
     */
    void ping() throws RemoteException;

    /**
     * Method to update servers with an event.
     * @param client the client that sends the event
     * @param event the event that modifies the model
     * @throws RemoteException default rmi exception
     */
    void update(Client client, GenericEvent event) throws RemoteException;

}
