package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.*;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.*;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Listener from model to view, i.e., server to client.
 */
public class ModelViewListener extends Listener {

    /**
     * Attribute containing the last request sent.
     */
    private GenericRequest lastRequest;
    /**
     * Attribute representing the number of requests sent without the corresponding response.
     * In order for the listener to send a new request, this must be zero.
     */
    private int requestEventIndex = 0;
    /**
     * The client associated with the listener.
     */
    private final Client client;
    /**
     * The nickname of the user associated with the listener.
     */
    public String nickname;
    /**
     * The list of chat messages.
     */
    private final Queue<ChatMessage> chatMessages = new LinkedList<ChatMessage>();
    /**
     * The server bound to this specific listener.
     * The listener will pass the information to the server which will likewise, pass it to the client
     */
    private final ServerImpl server;

    /**
     * Constructor
     *
     * Class that represents the listener situated between the Model and the View.
     * This listener will receive updates from the model and will pass them to the specific view, which will be updated aswell.
     * @param server the server that will receive the updates from this listener
     * @param client the client that sends the updates to this listener
     * @throws RemoteException default rmi exception
     */
    public ModelViewListener(ServerImpl server, Client client) throws RemoteException {
        this.server = server;
        this.client = client;
        this.nickname = client.getNickname();
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
                while(running) {
                    synchronized (lock_queue) {

                        if(!chatMessages.isEmpty()) {
                            try {
                                if(!(client instanceof ClientSkeleton)) server.logger.addLog("CHAT", Severity.SENDING);
                                client.update(chatMessages.poll());
                                if(!(client instanceof ClientSkeleton)) server.logger.addLog("CHAT", Severity.SENT);
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(ack != null){
                            try {
                                client.update(ack);

                                if(!ack.ok){
                                    client.update(lastRequest);

                                    requestEventIndex++;
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            requestEventIndex--;
                            ack = null;
                        }
                        else if(!getEventQueue().isEmpty()){
                            GenericEvent currentEvent = getEventQueue().poll(); //remove and return the first queue element

                            try{

                                if (requestEventIndex == 0 || currentEvent instanceof ServerMessage) {
                                    if (currentEvent instanceof GenericRequest) {
                                        lastRequest = (GenericRequest) currentEvent;
                                        requestEventIndex++;
                                    }

                                    if(!(client instanceof ClientSkeleton)){
                                        server.logger.addLog(currentEvent, Severity.SENDING);

                                        client.update(currentEvent);

                                        server.logger.addLog(currentEvent, Severity.SENT);
                                    }
                                    else client.update(currentEvent);

                                    if(currentEvent instanceof FinalRankings) ModelViewListener.this.stop();
                                    else if(currentEvent instanceof ErrorJoinLobby) server.controller.deleteClient(client);
                                }
                                else{
                                    getEventQueue().addFirst(currentEvent);
                                }
                            }catch(RemoteException e) {
                                if(!(currentEvent instanceof FinalRankings)) System.err.println("Can't communicate with " + nickname + ".");
                            }
                        }
                    }
                }
            }
        }.start();
    }

    /**
     * Method to add a chat message to the specific list.
     * @param message the received chat message
     */
    public void addChatMessage(ChatMessage message) {
        synchronized (lock_queue) {
            chatMessages.add(message);
        }
    }

    /**
     * Method to stop the running threads.
     */
    public void stop(){
        running = false;
    }

    /**
     * Method to handle the event to be sent to the corresponding client.
     * @param event event to be handled.
     */
    @Override
    public synchronized void addEvent(GenericEvent event) {
        // This is for stopping the check for 0/1 remaining player situation.
        if(event instanceof FinalRankings)
            server.controller.getGame().stop();

        synchronized (lock_queue) {
            if(event instanceof AckResponse)        ack = (AckResponse) event;
            else if(event instanceof ChatMessage)   chatMessages.add((ChatMessage) event);
            else if(event instanceof ServerMessage) getEventQueue().addFirst(event);
            else                                    getEventQueue().add(event);
        }

    }
}
