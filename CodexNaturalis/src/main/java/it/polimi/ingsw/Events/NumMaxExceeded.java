package it.polimi.ingsw.Events;

public class NumMaxExceeded extends GenericEvent {
    private final String msg = "The lobby is full.";

    @Override
    public String toString(){
        return msg;
    }
}
