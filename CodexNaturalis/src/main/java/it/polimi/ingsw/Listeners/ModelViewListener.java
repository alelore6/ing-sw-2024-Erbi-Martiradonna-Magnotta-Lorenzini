package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.*;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.ModelView.PlayerView;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class ModelViewListener extends Listener {


    private GenericRequest lastRequest;

    private int requestEventIndex = 0;
    public final Client client;
    private final Queue<ChatMessage> chatMessages = new LinkedList<ChatMessage>();
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
                                //TODO rimando anche l'evento da rifare (se non chat: in tal caso semplicemente stampare errore.)

                                //if(!ack.ok) client.update(ack.event); //cosi rimando la risposta non la richiesta!

                                if(!ack.ok){
//                                    switch(ack.event){
//                                        case PlayCardResponse e:
//
//                                            client.update(new PlayCardRequest(client.getNickname(), new PlayerView(server.controller.getPlayerByNickname(ack.nickname))));
//                                            break;
//                                        default:
//                                            throw new IllegalStateException("Unexpected value: " + ack.event);
//                                    }
                                    //TODO la seconda volta di fila che giochi male la carta non viene rimandato l'ack.
                                    // (o forse viene mandato ma non viene gestito in tempo)
                                    // in verità appena giochi male una carta, il client si stacca dal flusso di gioco

                                    client.update(lastRequest);
                                    requestEventIndex++;
                                }
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            if(!(ack.event instanceof ChatMessage)) requestEventIndex--;
                            ack = null;
                        }
                        else if(requestEventIndex == 0 && !getEventQueue().isEmpty()){
                            GenericEvent currentEvent = getEventQueue().poll(); //remove and return the first queue element

                            try{

                                if (requestEventIndex == 0 || currentEvent instanceof GenericRequest) {
                                    if (currentEvent instanceof GenericRequest) {
                                        lastRequest = (GenericRequest) currentEvent;
                                    }
                                    if(!(client instanceof ClientSkeleton)) server.logger.addLog(currentEvent, Severity.SENDING);
                                    client.update(currentEvent);
                                    if(!(client instanceof ClientSkeleton)) server.logger.addLog(currentEvent, Severity.SENT);
                                }
                                else{
                                    getEventQueue().offer(currentEvent);
                                }
                            }catch(RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            if(currentEvent instanceof GenericRequest){
                                requestEventIndex++;
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
}
