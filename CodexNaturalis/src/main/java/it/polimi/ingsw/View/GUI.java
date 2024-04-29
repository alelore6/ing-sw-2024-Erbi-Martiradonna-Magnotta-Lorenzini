package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Generic;

import java.rmi.RemoteException;

public class GUI extends UI{

    public GUI(String nickname) {
        super(nickname);
    }

    @Override
    public void handleModelMessage(Generic msg) {

    }

    @Override
    public void update(View view, Generic e) throws RemoteException {

    }

    @Override
    public void notifyAll(Generic e) {

    }

    @Override
    public void run() {

    }
}
