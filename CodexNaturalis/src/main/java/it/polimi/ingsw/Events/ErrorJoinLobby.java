package it.polimi.ingsw.Events;

/**
 * Event that represent an error in joining the pre-game lobby
 */
public class ErrorJoinLobby extends GenericEvent {
    /**
     * Constructor
     * @param nickname the player that receives the event
     */
    public ErrorJoinLobby(String nickname) {
        super("Cannot join a lobby: game has already started or lobby is full.", nickname);
    }
}
