package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;

public class TUI extends UI {

    public TUI(String nickname) {
        super(nickname);


    }

    @Override
    public void update(View v, GenericEvent e) throws RemoteException {
        //TODO update view based on receiving event
        //write a message describing the event and request a input
    }


    public void notifyListeners(View view, GenericEvent e) {

    }

    @Override
    public void run() {
        //keep view displaying? maybe useless in TUI
    }

    @Override
    public void handleModelMessage(GenericEvent msg) {

    }
}
