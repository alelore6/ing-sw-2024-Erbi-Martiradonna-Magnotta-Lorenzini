package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.Model.Card;
import it.polimi.ingsw.Model.ObjectiveCard;

import java.util.Deque;
import java.util.LinkedList;

public abstract class UI implements View{

    protected Deque<GenericEvent> inputEvents = new LinkedList<>();
    protected final ClientImpl client;
    protected final ViewControllerListener listener;
    protected volatile boolean isActive = true;

    public UI(ClientImpl client) {
        this.client = client;
        this.listener = new ViewControllerListener(client);
    }

    protected abstract void printCard(Card card);
    protected abstract void printCard(ObjectiveCard card);

    public ViewControllerListener getListener() {
        return listener;
    }

    public abstract String chooseNickname();

    public abstract void printErr(String s);
    public abstract void printOut(String s);
}
