package it.polimi.ingsw.Events;

public class AckResponse extends GenericEvent{
    public boolean ok;
    public AckResponse(boolean ok, String eventType,String nickname){
        super("",nickname);
        this.ok=ok;
        if(ok)
            message=eventType + " has gone successfully";
        else message=eventType+" has gone wrong. Please try again";
    }
}
