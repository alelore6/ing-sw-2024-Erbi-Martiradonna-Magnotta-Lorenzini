package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

public class TextualUI extends UI {

    @Override
    public void update(View v, Generic e) throws RemoteException {
        //TODO update view based on receiving event
        //write a message describing the event and request a input
    }

    @Override
    public void notifyListeners(View view, Generic e) {

    }

    @Override
    public void run() {
        //keep view displaying? maybe useless in TUI
    }

    @Override
    public void handleViewMsg(Generic msg) {

    }
}
