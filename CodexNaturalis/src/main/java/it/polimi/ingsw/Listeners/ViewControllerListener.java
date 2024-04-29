package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Events.Generic;

public interface ViewControllerListener {

    void handleViewMsg(Generic msg);
}
