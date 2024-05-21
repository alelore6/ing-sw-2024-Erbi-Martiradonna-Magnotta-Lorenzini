package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;

public interface RemoteClientInterface {
    void receiveObject(GenericEvent event) throws RemoteException;
}
