package it.polimi.ingsw.Events;

public class SetNumPlayers extends GenericEvent{
    private int numPlayers;
    public SetNumPlayers(){
        message="You are the first player in the lobby. Please set the number of players for the game between 2 and for 4";
    }
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
