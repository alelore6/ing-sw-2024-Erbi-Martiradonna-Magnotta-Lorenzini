package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Card;

import java.rmi.RemoteException;

public class GUI extends UI{

    public GUI(String nickname) {
        super(nickname);
    }

    @Override
    protected void printCard(Card card) {

    }

    @Override
    public void update(GenericEvent e) throws RemoteException {

    }

    @Override
    public void notifyListeners(ViewControllerListener listener, GenericEvent e) {

    }

    @Override
    public void run() {

    }
}
