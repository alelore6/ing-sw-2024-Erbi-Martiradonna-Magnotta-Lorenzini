package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;

public class GUI extends UI{

    public GUI(String nickname) {
        super(nickname);
    }

    @Override
    public void handleModelMessage(GenericEvent msg) {

    }

    @Override
    public void update(View view, GenericEvent e) throws RemoteException {

    }

    @Override
    public void notifyAll(GenericEvent e) {

    }

    @Override
    public void run() {

    }
}
