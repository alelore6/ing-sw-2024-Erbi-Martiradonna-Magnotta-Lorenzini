package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.GenericEvent;

import java.awt.*;
import java.rmi.RemoteException;

public class ModelViewListener extends Listener {

    /**
     * the server bound to this specific listener.
     * The listener will pass the information to the server which will likewise, pass it to the client
     */
    private final ServerImpl server;


    /**
     * Class that represents the listener situated between the Model and the View.
     * This listener will receive updates from the model and will pass them to the specific view, which will be updated aswell.
     * @param server the server that will receive the updates from this listener
     */
    public ModelViewListener(ServerImpl server) {
        this.server = server;
    }

    /**
     * method override from abstract super class Listener
     * It will pass it to the server and then if the event was indeed received correctly,
     * it will finally remove it from the queue.
     * @throws RemoteException remote exception for RMI connection
     */
    @Override
    public void handleEvent() throws RemoteException {
        GenericEvent currentEvent = getEventQueue().remove(); //remove and return the first queue element

        //handles the message passing it to the Server which will transfer it to the Client
        server.sendEvent(currentEvent);
        //TODO è possibile che non vadano a buon fine gli eventi?
        // Necessaria l'introduzione di un ack prima di rimuovere effettivamente l'elemento dalla coda?
    }
}
