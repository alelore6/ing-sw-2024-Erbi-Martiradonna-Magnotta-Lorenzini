package it.polimi.ingsw.Network.Client;

import it.polimi.ingsw.Listener.ModelViewListener;
import it.polimi.ingsw.Listener.ViewControllerListener;
import it.polimi.ingsw.Network.Server.GameServer;

import javax.swing.text.View;

public class Client implements Runnable{
    //represent the player
    public final String nickname;
    private View view;
    private int viewType; //1 if GUI, 0 if CLI
    private int networkType; // 0 rmi, 1 socket
    private ViewControllerListener vcListener;
    private ModelViewListener mvListener;
    public Client(String nickname, int viewType,int networkType, GameServer server){
        this.vcListener=new ViewControllerListener(server.controller,this);
        this.nickname=nickname;
        this.viewType=viewType;
        this.networkType=networkType;
        // create view based on view type

    }
    @Override
    public void run() {
    //keep view running
    }
}
