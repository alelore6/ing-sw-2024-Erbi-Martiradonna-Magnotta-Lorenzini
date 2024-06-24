package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.*;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.*;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class ModelViewListener extends Listener {

    private GenericRequest lastRequest;
    private int requestEventIndex = 0;
    private volatile boolean endSent = false;
    public final Client client;
    public String nickname;
    private final Queue<ChatMessage> chatMessages = new LinkedList<ChatMessage>();
    /**
     * the server bound to this specific listener.
     * The listener will pass the information to the server which will likewise, pass it to the client
     */
    public final ServerImpl server;

    /**
     * Constructor
     *
     * Class that represents the listener situated between the Model and the View.
     * This listener will receive updates from the model and will pass them to the specific view, which will be updated aswell.
     * @param server the server that will receive the updates from this listener
     */
    public ModelViewListener(ServerImpl server, Client client) throws RemoteException {
        this.server = server;
        this.client = client;
        this.nickname = client.getNickname();
    }

    /**
     * This constructor is only for stopping the server when no one is playing anymore.
     * In fact, it receives the FinalRankings event that restarts the server.
     */
    public ModelViewListener(ServerImpl server){
        this.server = server;
        this.client = null;
        this.nickname = null;
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

                                    if(currentEvent instanceof FinalRankings) endSent = true;
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

    public int getRequestEventIndex() {
        return requestEventIndex;
    }

    public void setRequestEventIndex(int requestEventIndex) {
        this.requestEventIndex = requestEventIndex;
    }

    public void addChatMessage(ChatMessage message) {
        synchronized (lock_queue) {
            chatMessages.add(message);
        }
    }

    public void stop(){
        running = false;
    }

    @Override
    public synchronized void addEvent(GenericEvent event) {
        synchronized (lock_queue) {
            if(event instanceof AckResponse)        ack = (AckResponse) event;
            else if(event instanceof ChatMessage)   chatMessages.add((ChatMessage) event);
            else if(event instanceof ServerMessage) getEventQueue().addFirst(event);
            else                                    getEventQueue().add(event);
        }

        if(event instanceof FinalRankings){

            while(!endSent){}

            server.notifyEndSent();

            if(server.getEndSent() >= server.controller.getMVListeners().size()) server.restart();
        }
    }
}
