package it.polimi.ingsw.Listener;

import it.polimi.ingsw.Messages.Generic;

public interface ViewListener {

    void handleViewMsg(Generic msg);

    /*// listens to view events and acts on the controller
    Controller controller;
    ClientImpl clientImpl;

    public ViewControllerListener(Controller controller, ClientImpl clientImpl){
        this.controller = controller;
        this.clientImpl = clientImpl;
    }

    public void receiveEvent(){

    }*/
}
