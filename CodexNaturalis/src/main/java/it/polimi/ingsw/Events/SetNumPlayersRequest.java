package it.polimi.ingsw.Events;

public class SetNumPlayersRequest extends GenericEvent{
    public SetNumPlayersRequest(String nickname){
        super("You are the first player in the lobby. Please set the number of players for the game between 2 and for 4",nickname);
    }
}
