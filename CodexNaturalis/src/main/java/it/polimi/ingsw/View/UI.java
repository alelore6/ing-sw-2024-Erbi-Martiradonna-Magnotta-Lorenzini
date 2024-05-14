package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.Model.Card;

import java.util.Deque;

public abstract class UI implements View{

    protected Deque<GenericEvent> inputMessages;
    protected final ClientImpl client;
    protected final ViewControllerListener listener;

    // WATCH OUT! This constructor allows the creation of a TUI w/o having created the actual player
    // The nickname here is not the player's but a test one.
    public UI() {
        this.client = null;
        this.listener = null;
    }

    public UI(ClientImpl client) {
        this.client = client;
        this.listener = new ViewControllerListener(client);
    }

    protected abstract void printCard(Card card);

    public ViewControllerListener getListener() {
        return listener;
    }
}
