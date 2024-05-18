package it.polimi.ingsw.Events;

/**
 * Event that represent the request to set the number of players for the game
 */
public class NumPlayersRequest extends GenericEvent{
    private static final long serialVersionUID = 13L;
    /**
     * Constructor
     * @param nickname the player that receives the event
     */
    public NumPlayersRequest(String nickname){
        super("You are the first player in the lobby, please set the number of players for the game.\n",nickname);
    }

    @Override
    public String msgOutput() {
        return super.msgOutput() + "Enter a number between 2 and 4";
    }
}
