package it.polimi.ingsw.View;

import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

public interface View extends Runnable {

    public void update(View view, Generic e) throws RemoteException;

    public void notifyListeners(View view, Generic e);

    public void addListener(View v, ViewControllerListener listener);

    void run();
}
