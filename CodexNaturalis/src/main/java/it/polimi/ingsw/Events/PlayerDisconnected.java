package it.polimi.ingsw.Events;

import it.polimi.ingsw.Model.Game;

/**
 * event that communicate to the other players that someone has disconnected/reconnected
 */
public class PlayerDisconnected extends ServerMessage{

    /**
     * Constructor
     * @param nickname the player that receives the event
     * @param disconnectedPlayer the player that has disconnected/ reconnected
     * @param remainingPlayers number of remaining players in the game
     * @param isRejoined true if the player has reconnected, false if in case of disconnection
     */
    public PlayerDisconnected(String nickname, String disconnectedPlayer, int remainingPlayers, boolean isRejoined) {
        super(disconnectedPlayer + " has " + (isRejoined ? "rejoined, " : "disconnected" + (remainingPlayers == 1 ? ", "+ remainingPlayers+" player left.\nIf no one reconnects in " + Game.timeoutOnePlayer + " seconds, you automatically win!\nWaiting for a player to reconnect..." : " players in lobby.\n")) , nickname);

        if (remainingPlayers==1) message.concat("Waiting for a player to rejoin the game.");
    }
}
