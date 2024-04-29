package it.polimi.ingsw.View;

import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Listeners.ViewControllerListener;

import java.rmi.RemoteException;

public interface View extends Runnable {

    void update(View view, Generic e) throws RemoteException;

    void notifyAll(Generic e);

    void addListener(ViewControllerListener listener);

    void run();
}
