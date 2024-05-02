package it.polimi.ingsw.Events;

public class YourTurn extends GenericEvent{
    public YourTurn(String nickname, String turnPlayer){
        super("It's "+ turnPlayer+ "'s turn!",nickname);
    }
}
