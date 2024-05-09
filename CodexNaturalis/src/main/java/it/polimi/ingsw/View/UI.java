package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Card;

import java.util.ArrayList;

public abstract class UI implements View{

    protected final ClientImpl client;
    protected final ViewControllerListener listener;

    // WATCH OUT! This constructor allows the creation of a TUI w/o having created the actual player
    // The nickname here is not the player's but a test one.
    public UI(String nickname) {
        this.client = null;
        this.listener = null;
    }

    protected abstract void printCard(Card card);

    public UI(ClientImpl client) {
        this.client = client;
        this.listener = new ViewControllerListener(client);
    }
}
