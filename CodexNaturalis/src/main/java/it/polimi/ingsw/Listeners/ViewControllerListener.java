package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;

public class ViewControllerListener extends Listener {

    private Client client;

    public ViewControllerListener(Client client) {
        this.client = client;
    }


    @Override
    public void handleEvent() throws RemoteException {

        while (!getEventQueue().isEmpty()) {
            GenericEvent currentEvent = getEventQueue().poll(); //remove and return the first queue element

            client.update(client, currentEvent);
            //TODO è possibile che non vadano a buon fine gli eventi?
            // Necessaria l'introduzione di un ack prima di rimuovere effettivamente l'elemento dalla coda?
        }
    }
}
