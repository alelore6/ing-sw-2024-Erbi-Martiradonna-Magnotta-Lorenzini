package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

public interface View extends Runnable {

    void update(View view, GenericEvent e) throws RemoteException;

    void notifyAll(GenericEvent e);

    void addListener(ViewControllerListener listener);

    void run();
}
