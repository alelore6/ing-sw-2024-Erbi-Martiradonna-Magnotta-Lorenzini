package it.polimi.ingsw.Events;

public class YourTurn extends GenericEvent{
    public YourTurn(String nickname){
        super("It's your turn!",nickname);
    }
}
