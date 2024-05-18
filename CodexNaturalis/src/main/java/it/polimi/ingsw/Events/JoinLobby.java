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
        super("You have joined a lobby, waiting for other players to start the game.\nPlease, set a password so that " +
                "you can reconnect to this game in case of a disconnection.\n Nickname: '"+nickname+"'", nickname);
    }
}
