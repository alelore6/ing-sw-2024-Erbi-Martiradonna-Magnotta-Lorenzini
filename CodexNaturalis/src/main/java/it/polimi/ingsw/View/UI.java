package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class UI implements View, ViewControllerListener {

    protected ArrayList<ViewControllerListener> listeners;

    @Override
    public void addListener(View v, ViewControllerListener listener){
        listeners.add(listener);
    }

    public abstract void run();
}
