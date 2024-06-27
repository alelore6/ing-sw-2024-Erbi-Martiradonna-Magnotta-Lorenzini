package it.polimi.ingsw.Exceptions;

/**
 * exception that is thrown when a player cant be found
 */
public class PlayerNotFoundException extends Throwable {
    /**
     * the nickname of the player that cant be found
     */
    public String nickname;

    /**
     * Constructor
     * @param nickname the nickname of the player that cant be found
     */
    public PlayerNotFoundException(String nickname) {
        this.nickname = nickname;
    }
}
