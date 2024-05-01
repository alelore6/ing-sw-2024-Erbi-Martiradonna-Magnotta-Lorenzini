package it.polimi.ingsw.Events;

public class NumPlayersRequest extends GenericEvent{
    public NumPlayersRequest(String nickname){
        super("You are the first player in the lobby, please set the number of players for the game.\n Enter a number between 2 and 4",nickname);
    }
}
