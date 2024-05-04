package it.polimi.ingsw.Events;

public class AckResponse extends GenericEvent{
    public final boolean ok;

    public AckResponse(boolean ok, String eventType,String nickname){
        super(ok ? eventType + " has gone successfully" : eventType + " has gone wrong. Please try again", nickname);
        this.ok = ok;
    }
}
