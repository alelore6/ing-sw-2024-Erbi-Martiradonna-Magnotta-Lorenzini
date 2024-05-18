package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.Model.Card;

import java.util.Deque;

public abstract class UI implements View{

    protected Deque<GenericEvent> inputEvents;
    protected final ClientImpl client;
    protected final ViewControllerListener listener;

    public UI(ClientImpl client) {
        this.client = client;
        this.listener = new ViewControllerListener(client);
    }

    protected abstract void printCard(Card card);

    public ViewControllerListener getListener() {
        return listener;
    }

    public abstract String setNickname();

    public abstract void printErr(String s);
    public abstract void printOut(String s);
}
