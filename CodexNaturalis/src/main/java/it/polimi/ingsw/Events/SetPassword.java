package it.polimi.ingsw.Events;

/**
 * Event for setting the password after joining a lobby
 */
public class SetPassword extends GenericResponse{
    /**
     * the entered password
     */
    private final String password;

    /**
     * Constructor
     * @param nickname the receiver of the event
     * @param password the entered password
     */
    public SetPassword(String nickname, String password) {
        super("Password set! Waiting for other players to join the lobby...", nickname);
        this.password = password;
    }

    /**
     * Getter for the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
