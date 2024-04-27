package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Listener.ModelViewListener;
import it.polimi.ingsw.Listener.ViewControllerListener;

import javax.swing.text.View;
import java.rmi.RemoteException;

public class ClientImpl implements Runnable, Client{

    TextualUI view = new TextualUI();
    //represent the player
    public final String nickname;


    private int viewType; //1 if GUI, 0 if CLI
    private int networkType; // 0 rmi, 1 socket
    private ViewControllerListener vcListener;
    private ModelViewListener mvListener;
    public ClientImpl(Server server){
        view.addObserver((v, e) -> server.update(this, e));
    }


    @Override
    public void run() {
        view.run();
    }



    @Override
    public void update(PreviousView v, Event e) throws RemoteException {
        this.view.update(v, e);
    }
}
