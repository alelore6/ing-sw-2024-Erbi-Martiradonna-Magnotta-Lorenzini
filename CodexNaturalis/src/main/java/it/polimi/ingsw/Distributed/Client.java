package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.View;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that describes the server structure. Classes that implement it can be viewed as servers.
 */
public interface Client extends Remote{

    /**
     * Method to pong with the server.
     * @throws RemoteException default rmi exception
     */
    void ping() throws RemoteException;

    /**
     * Method to update clients with an event.
     * @param event the received event
     * @throws RemoteException default rmi exception
     */
    void update(GenericEvent event) throws RemoteException;

    /**
     * Getter fot the nickname.
     * @return the nickname.
     * @throws RemoteException default rmi exception
     */
    String getNickname() throws RemoteException;

    /**
     * Setter for the nickname.
     * @param nickname the entered nickname
     * @throws RemoteException default rmi exception
     */
    void setNickname(String nickname) throws RemoteException;
}
