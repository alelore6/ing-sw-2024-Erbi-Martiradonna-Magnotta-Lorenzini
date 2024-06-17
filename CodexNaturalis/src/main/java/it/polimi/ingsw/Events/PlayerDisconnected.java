package it.polimi.ingsw.Events;

public class PlayerDisconnected extends ServerMessage{

    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public PlayerDisconnected(String nickname, String disconnectedPlayer, int remainingPlayers, boolean isRejoined) {
        super(disconnectedPlayer + " has " + (isRejoined == true ? "rejoined." : ("disconnected, " + remainingPlayers + (remainingPlayers == 1 ? " player " : " players ") + "left.\n")), nickname);
        mustBeSentToAll=true;
        if (remainingPlayers==1) message.concat("Waiting for a player to rejoin the game.");
    }
}
