package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.util.Deque;
import java.util.LinkedList;

/**
 * generic interface representing the UI
 */
public abstract class UI implements View{
    /**
     * the queue of incoming events
     */
    final Deque<GenericEvent> inputEvents = new LinkedList<>();
    /**
     * the client that owns the UI
     */
    final ClientImpl client;
    /**
     * the listener to communicate with the server
     */
    final ViewControllerListener listener;
    /**
     * boolean representing the running status of the UI
     */
    public volatile boolean running = true;
    /**
     * the nickname of the owner of the UI
     */
    String nickname;

    /**
     * Constructor
     * @param client the client that owns the UI
     */
    UI(ClientImpl client) {
        this.client = client;
        this.listener = new ViewControllerListener(client);
    }

    /**
     * Getter for the listener.
     * @return the listener.
     */
    public ViewControllerListener getListener() {
        return listener;
    }

    /**
     * Method to send the event designated to the server controller.
     * @param event the event sent to the listener.
     * @see ViewControllerListener
     */
    public final void notifyListener(GenericEvent event) {
        listener.addEvent(event);
    }

    /**
     * allow the player to set his nickname
     * @return the chosen nickname
     * @see GUI
     * @see TUI
     */
    public abstract String chooseNickname();

    /**
     * print errors in the console
     * @param s the error message
     */
    public abstract void printErr(String s);

    /**
     * print messages in the console
     * @param s the message
     */
    public abstract void printOut(String s);
}
