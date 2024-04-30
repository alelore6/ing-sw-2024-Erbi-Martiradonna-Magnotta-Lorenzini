package it.polimi.ingsw.Events;

public class StartGame extends GenericEvent{
    public StartGame(String nickname){
        super("The game has started",nickname);
    }
}
