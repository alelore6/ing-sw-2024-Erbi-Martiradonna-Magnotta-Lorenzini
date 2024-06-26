package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

/**
 * represent a generic handler for user interface
 */
public interface View extends Runnable {

    void update(GenericEvent e);

    void notifyListener(GenericEvent e);

    void run();
}
