package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.Generic;

import java.rmi.RemoteException;

public class TextualUI extends ClientImpl {

    @Override
    public void update(View v, Generic e) throws RemoteException {
        //TODO update view based on receiving event
        //write a message describing the event and request a input
    }

    @Override
    public void run() {
        //keep view displaying? maybe useless in TUI
    }

}
