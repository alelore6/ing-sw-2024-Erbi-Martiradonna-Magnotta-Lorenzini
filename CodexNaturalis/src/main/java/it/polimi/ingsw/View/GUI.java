package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

public class GUI extends UI{

    public GUI(String nickname) {
        super(nickname);
    }

    @Override
    public void update(GenericEvent e) throws RemoteException {

    }

    @Override
    public void notifyAll(GenericEvent e) {

    }

    @Override
    public void addListener(ViewControllerListener listener) {

    }

    @Override
    public void run() {

    }
}
