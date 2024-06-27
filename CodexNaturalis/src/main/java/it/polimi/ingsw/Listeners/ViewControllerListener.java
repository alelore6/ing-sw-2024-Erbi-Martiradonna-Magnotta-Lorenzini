package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.AckResponse;
import it.polimi.ingsw.Events.FinalRankings;
import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;

/**
 * Listener between the player UI and the server
 */
public class ViewControllerListener extends Listener {

    /**
     * attribute representing the client bound to this listener.
     * Every view has a different client and so, a specific listener.
     */
    private final ClientImpl client;

    /**
     * Class representing the listener situated between the view and the controller.
     * When the view is updated by the user, the listener will receive a specific event,
     * sending this event through the network to the controller class.
     * @param client Client needed to pass the update to.
     */
    public ViewControllerListener(ClientImpl client) {
        this.client = client;
    }


    /**
     * method override from abstract super class Listener
     * this method will continuously handle incoming events as long as the queue isn't empty.
     * It will pass it to the client and then if the event was indeed received correctly,
     * it will finally remove it from the queue.
     * @throws RemoteException remote exception for RMI connections
     */
    @Override
    public void handleEvent() throws RemoteException {
        eventThread = new Thread() {
            @Override
            public void run() {
                while(running) {
                    synchronized(lock_queue){
                        if(ack != null){
                            AckResponse response = ack;
                            ack = null;

                            try {
                                client.sendEvent(response);
                            } catch (RemoteException ignored) {}

                            if(response instanceof AckResponse && response.receivedEvent instanceof FinalRankings) {
                                client.clientApp.stop();
                            }
                        }
                        else if(!getEventQueue().isEmpty()) {
                            GenericEvent currentEvent = getEventQueue().remove(); //remove and return the first queue element
                            // System.out.println("MANDATO EVENTO: " + currentEvent.msgOutput());
                            try {
                                client.sendEvent(currentEvent);
                            } catch (RemoteException e) {
                                if(client.getUserInterface().running){
                                    e.printStackTrace();
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        };

        eventThread.start();
    }
}
