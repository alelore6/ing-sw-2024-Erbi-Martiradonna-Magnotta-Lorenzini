package it.polimi.ingsw.Events;

public class ReconnectionResponse extends GenericResponse{
    private final String password;
    /**
     * Constructor
     *
     * @param message  message describing the event
     * @param nickname player that receives or sends the event
     */
    public ReconnectionResponse(String nickname, String password) {
        super("Password entered", nickname);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
