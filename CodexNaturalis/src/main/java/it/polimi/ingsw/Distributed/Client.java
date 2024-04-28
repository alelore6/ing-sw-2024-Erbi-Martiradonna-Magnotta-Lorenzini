package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Messages.Events;
import it.polimi.ingsw.View.View;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {


    void update(View view, Events e) throws RemoteException;
}
