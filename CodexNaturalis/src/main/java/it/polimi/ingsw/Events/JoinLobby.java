package it.polimi.ingsw.Events;

/**
 * Event that represent the successful join of the pre-game lobby of a player
 */
public class JoinLobby extends GenericEvent{
    /**
     * Constructor
     * @param nickname the player that has joined the lobby
     */
    public JoinLobby(String nickname){
        super("You have joined a lobby. Please, set a password so that " +
                "you can reconnect to this game after a disconnection.", nickname);
    }
}
