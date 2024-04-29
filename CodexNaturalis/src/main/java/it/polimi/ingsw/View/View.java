package it.polimi.ingsw.View;

import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.Events.Generic;

import java.rmi.RemoteException;

public interface View extends Runnable {

    void update(View view, Generic e) throws RemoteException;

    void notifyListeners(View view, Generic e);

    void addListener(ModelViewListener listener);

    @Override
    void run();


}
