package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Card;

import java.rmi.RemoteException;

public class GUI extends UI{

    public GUI(ClientImpl client) {
        super(client);
    }

    @Override
    protected void printCard(Card card) {

    }

    @Override
    public void update(GenericEvent e){

    }

    @Override
    public void notifyListener(ViewControllerListener listener, GenericEvent e) {

    }

    @Override
    public void run() {

    }
}
