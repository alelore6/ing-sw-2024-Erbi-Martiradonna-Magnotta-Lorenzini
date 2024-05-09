package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

public interface View extends Runnable {

    void update(GenericEvent e) throws RemoteException;

    void notifyListener(ViewControllerListener listener, GenericEvent e);

    void run();
}
