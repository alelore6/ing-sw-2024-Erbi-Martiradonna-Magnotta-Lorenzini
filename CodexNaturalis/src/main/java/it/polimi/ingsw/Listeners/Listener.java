package it.polimi.ingsw.Listeners;

import it.polimi.ingsw.Events.AckResponse;
import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Listener {


    protected Object lock_queue = new Object();

    protected AckResponse ack = null;

    /**
     * attribute representing the queue of generic events
     * that will be handled by the listener
     */
    private final Queue<GenericEvent> eventQueue = new LinkedList<GenericEvent>();

    /**
     * method getter for the event queue of this specific listener
     * @return Queue of Generic Events
     */
    public Queue<GenericEvent> getEventQueue() {
        return eventQueue;
    }

    /**
     * abstract method to be implemented by listener subclasses to handle events
     * @throws RemoteException remote exception for RMI connections
     */
    public abstract void handleEvent() throws RemoteException;

    /**
     * method to add an event to the queue of events
     * @param event event to be added to the queue
     */
    public void addEvent(GenericEvent event) {
        synchronized (lock_queue) {
            if (event instanceof AckResponse) {
                ack = (AckResponse) event;
            } else {
                eventQueue.add(event);
            }
        }
    }
}
