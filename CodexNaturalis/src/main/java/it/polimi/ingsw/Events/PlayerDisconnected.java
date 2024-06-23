package it.polimi.ingsw.Events;

public class PlayerDisconnected extends ServerMessage{

    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public PlayerDisconnected(String nickname, String disconnectedPlayer, int remainingPlayers, boolean isRejoined) {
        super(disconnectedPlayer + " has " + (isRejoined == true ? "rejoined, " : "disconnected, "+ remainingPlayers + (remainingPlayers == 1 ? " player left.\nIf no one reconnects in 30 seconds, you automatically win!\nWaiting for a player to reconnect..." : " players in lobby.\n")) , nickname);

        if (remainingPlayers==1) message.concat("Waiting for a player to rejoin the game.");
    }
}
