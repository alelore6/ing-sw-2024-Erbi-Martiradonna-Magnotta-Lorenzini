package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.View;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote{


    void update(GenericEvent e) throws RemoteException;
}
