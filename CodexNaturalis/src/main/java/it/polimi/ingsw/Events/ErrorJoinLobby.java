package it.polimi.ingsw.Events;

/**
 * Event that represent an error in joining the pre-game lobby
 */
public class ErrorJoinLobby extends GenericEvent {
    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param c the type of error:
     *          0 if the lobby is full,
     *          1 if the lobby doesn't exist,
     *          2 if the game has already started
     */
    public ErrorJoinLobby(String nickname, int c) {
        super("Cannot join a lobby: " + (c == 1 ? "it doesn't exist yet!" :
                (c == 2 ? "the game has already started!" : "it's full!")), nickname);
    }
}
