package it.polimi.ingsw.Listener;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.controller.Controller;

public class ViewControllerListener {
    //listen to view events and act on the controller
    Controller controller;
    ClientImpl clientImpl;
    public ViewControllerListener(Controller controller, ClientImpl clientImpl){
        this.controller=controller;
        this.clientImpl = clientImpl;
    }

    public void receiveEvent(){

    }
}
