package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

/**
 * represent a generic handler for user interface
 */
public interface View extends Runnable {
    /**
     * Updates the view based on the received event
     * @param e the received event
     * @see GUI
     * @see TUI
     */
    void update(GenericEvent e);

    /**
     * notify the view-controller listener that the player has made an action
     * @param e the event representing the action
     */
    void notifyListener(GenericEvent e);

    /**
     * where all the received event will be handled
     */
    void run();
}
