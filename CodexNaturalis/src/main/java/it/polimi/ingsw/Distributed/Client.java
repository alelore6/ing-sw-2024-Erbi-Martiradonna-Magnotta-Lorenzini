package it.polimi.ingsw.Distributed;

import java.rmi.RemoteException;

public interface Client extends Remote {

    void update(PreviousView v, Event e) throws RemoteException;
}
