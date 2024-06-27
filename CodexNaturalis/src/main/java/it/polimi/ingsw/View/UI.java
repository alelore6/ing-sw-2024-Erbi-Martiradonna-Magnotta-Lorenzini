package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.util.Deque;
import java.util.LinkedList;

public abstract class UI implements View{

    final Deque<GenericEvent> inputEvents = new LinkedList<>();
    final ClientImpl client;
    final ViewControllerListener listener;
    public volatile boolean running = true;
    String nickname;

    /**
     * Constructor
     * @param client
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

    public abstract String chooseNickname();

    public abstract void printErr(String s);
    public abstract void printOut(String s);
}
