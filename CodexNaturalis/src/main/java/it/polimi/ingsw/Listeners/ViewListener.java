package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Messages.Generic;

public interface ViewListener {

    void handleViewMsg(Generic msg);

    /*// listens to view events and acts on the Controller
    Controller Controller;
    ClientImpl clientImpl;

    public ViewControllerListener(Controller Controller, ClientImpl clientImpl){
        this.Controller = Controller;
        this.clientImpl = clientImpl;
    }

    public void receiveEvent(){

    }*/
}
