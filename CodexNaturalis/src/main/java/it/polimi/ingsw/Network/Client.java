package it.polimi.ingsw.Network;

import it.polimi.ingsw.Listener.ModelViewListener;
import it.polimi.ingsw.Listener.ViewControllerListener;

import javax.swing.text.View;

public class Client implements Runnable{
    //represent the player
    public String nickname;
    private View view;
    int viewType; //1 if GUI, 0 if CLI
    int connectionType; //serve?
    ModelViewListener mvListener;
    ViewControllerListener vcListener;
    public Client(String nickname, int viewType, GameServer server){
        mvListener= new ModelViewListener(server.controller.GameModel,this);
        vcListener= new ViewControllerListener(server.controller,this);
        this.nickname=nickname;
        this.viewType=viewType;
        // create view based on view type

    }
    @Override
    public void run() {
    //keep view running
    }
}
