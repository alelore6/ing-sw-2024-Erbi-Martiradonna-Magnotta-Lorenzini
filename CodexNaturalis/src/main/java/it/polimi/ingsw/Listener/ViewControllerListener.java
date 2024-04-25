package it.polimi.ingsw.Listener;

import it.polimi.ingsw.Network.Client;
import it.polimi.ingsw.controller.Controller;

public class ViewControllerListener {
    //listen to view events and act on the controller
    Controller controller;
    Client client;
    public ViewControllerListener(Controller controller, Client client){
        this.controller=controller;
        this.client=client;
    }
}
