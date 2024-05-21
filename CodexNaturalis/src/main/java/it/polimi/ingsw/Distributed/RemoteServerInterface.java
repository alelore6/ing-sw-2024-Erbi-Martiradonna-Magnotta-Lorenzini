package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServerInterface extends Remote {

    public void processEvent(GenericEvent event, Client client) throws RemoteException;

    public void processClient(ClientImpl remoteClient) throws RemoteException;

}
