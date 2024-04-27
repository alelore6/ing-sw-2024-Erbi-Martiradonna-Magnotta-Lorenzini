package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Messages.Events;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {


    void update(Events e) throws RemoteException;
}
