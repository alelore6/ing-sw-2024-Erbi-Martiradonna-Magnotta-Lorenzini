package it.polimi.ingsw.Events;

public class NumPlayersResponse extends GenericEvent{
    public final int numPlayers;

    public NumPlayersResponse(int numPlayers, String nickname) {
        super("Number of players set!",nickname);
        this.numPlayers = numPlayers;
    }
}
