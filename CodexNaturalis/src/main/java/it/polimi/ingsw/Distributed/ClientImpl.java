package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Listener.ModelListener;
import it.polimi.ingsw.Listener.ViewListener;

import javax.swing.text.View;
import java.rmi.RemoteException;

public class ClientImpl implements Runnable, Client{

    TextualUI view = new TextualUI();
    //represent the player
    public final String nickname;
    private View view;
    private int viewType; //1 if GUI, 0 if CLI
    private int networkType; // 0 rmi, 1 socket
    private ViewListener vcListener;
    private ModelListener mvListener;
    public ClientImpl(String nickname, int viewType, int networkType, ServerImpl server){
        // this.vcListener=new ViewListener(server.controller,this);
        this.nickname=nickname;
        this.viewType=viewType;
        this.networkType=networkType;
        // create view based on view type

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
