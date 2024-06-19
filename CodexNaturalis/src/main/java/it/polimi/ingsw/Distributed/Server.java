package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {

    void ping() throws RemoteException;

    void update(Client client, GenericEvent event) throws RemoteException, InterruptedException;

}
