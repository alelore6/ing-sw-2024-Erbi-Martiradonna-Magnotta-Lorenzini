package it.polimi.ingsw.Events;

/**
 * Event that represent the response to a request to set the number of players for the game
 */
public class NumPlayersResponse extends GenericEvent{
    private static final long serialVersionUID = 14L;
    /**
     * the chosen number of players
     */
    public final int numPlayers;

    /**
     * Constructor
     * @param numPlayers the chosen number of players
     * @param nickname the player that sends the response
     */
    public NumPlayersResponse(int numPlayers, String nickname) {
        super("Number of players set!",nickname);
        this.numPlayers = numPlayers;
    }
}
