package it.polimi.ingsw.Events;

public class PlayerDisconnected extends GenericEvent{

    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public PlayerDisconnected(String playerNickname, String nickname, int remainingPlayers) {
        super(playerNickname+" has disconnected, "+remainingPlayers+" left.", nickname);
        mustBeSentToAll=true;
        if (remainingPlayers==1) message.concat(" Game is over. "+nickname+ " is the winner.");
    }
}
