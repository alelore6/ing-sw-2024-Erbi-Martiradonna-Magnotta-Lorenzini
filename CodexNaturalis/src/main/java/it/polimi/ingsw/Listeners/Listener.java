package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Listener {

    public Queue<GenericEvent> getEventQueue() {
        return eventQueue;
    }

    private Queue<GenericEvent> eventQueue = new LinkedList<GenericEvent>();

    public void handleEvent() throws RemoteException {
    }

    public void addEvent(GenericEvent event){
        eventQueue.add(event);
    }
}
