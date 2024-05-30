package it.polimi.ingsw.Events;

/**
 * Event that represent the request to set the number of players for the game
 */
public class NumPlayersRequest extends GenericRequest{
    /**
     * Constructor
     * @param nickname the player that receives the event
     */
    public NumPlayersRequest(String nickname){
        super("You are the first player in the lobby, please set the number of players for the game.",nickname);
    }

    @Override
    public String msgOutput() {
        return super.msgOutput() + "\nEnter a number between 2 and 4:";
    }
}
