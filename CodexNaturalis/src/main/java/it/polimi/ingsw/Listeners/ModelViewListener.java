package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.GenericEvent;

import java.awt.*;
import java.rmi.RemoteException;

public class ModelViewListener extends Listener {

    private ServerImpl server;

    public ModelViewListener(ServerImpl server) {
        this.server = server;
    }

    @Override
    public void handleEvent() throws RemoteException {
        GenericEvent currentEvent = getEventQueue().remove(); //remove and return the first queue element

        //handles the message passing it to the Server which will transfer it to the Client
        server.sendEvent(currentEvent);
        //TODO è possibile che non vadano a buon fine gli eventi?
        // Necessaria l'introduzione di un ack prima di rimuovere effettivamente l'elemento dalla coda?
    }
}
