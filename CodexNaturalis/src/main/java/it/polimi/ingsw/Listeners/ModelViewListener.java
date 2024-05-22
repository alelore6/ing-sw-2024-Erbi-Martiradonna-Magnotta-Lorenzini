package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.*;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;

public class ModelViewListener extends Listener {

    public final Client client;
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
    public ModelViewListener(ServerImpl server, Client client) {

        this.server = server;
        this.client = client;
    }

    /**
     * method override from abstract super class Listener
     * It will pass it to the server and then if the event was indeed received correctly,
     * it will finally remove it from the queue.
     * @throws RemoteException remote exception for RMI connection
     */
    @Override
    public void handleEvent() throws RemoteException {
        new Thread(){
            @Override
            public void run() {

                while(true) {
                    synchronized (lock_queue) {
                        if(ack != null){
                            try {
                                server.sendEventToAll(ack);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            ack = null;
                        }
                        else if(!getEventQueue().isEmpty()){
                            GenericEvent currentEvent = getEventQueue().poll(); //remove and return the first queue element

                            try {
                                if (currentEvent.mustBeSentToAll) {
                                    server.sendEventToAll(currentEvent);
                                }
                                else{
                                    Client c = server.findClientByNickname(currentEvent.nickname);
                                    if(c == null)   server.logger.addLog("Can't send event: user not found.", Severity.FAILURE);

                                    // the client is connected with socket
                                    else if(c instanceof ClientSkeleton)    server.sendEvent(server.findCSbyNickname(currentEvent.nickname), currentEvent);

                                    // the client is connected with RMI
                                    else server.sendEvent(server.findCPByNickname(currentEvent.nickname), currentEvent);
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }.start();
    }
}
