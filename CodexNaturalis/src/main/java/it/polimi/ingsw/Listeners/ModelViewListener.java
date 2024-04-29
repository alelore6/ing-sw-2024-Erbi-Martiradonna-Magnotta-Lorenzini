package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Events.GenericEvent;

public interface ModelViewListener {
    //devono avere una coda di eventi cosi da elaborarne uno alla volta senza problemi

    void handleModelMessage(GenericEvent event);
}
