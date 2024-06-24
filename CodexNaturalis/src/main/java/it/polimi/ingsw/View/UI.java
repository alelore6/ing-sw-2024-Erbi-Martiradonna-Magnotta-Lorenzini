package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.util.Deque;
import java.util.LinkedList;

public abstract class UI implements View{

    protected final Deque<GenericEvent> inputEvents = new LinkedList<>();
    protected final ClientImpl client;
    protected final ViewControllerListener listener;
    public volatile boolean running = true;
    protected String nickname;

    /**
     * Constructor
     * @param client
     */
    public UI(ClientImpl client) {
        this.client = client;
        this.nickname = client.getNickname();
        this.listener = new ViewControllerListener(client);
    }

    /**
     * Method to stop the running threads.
     */
    public void stop(){
        running = false;
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
