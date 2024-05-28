package it.polimi.ingsw.Events;

public class SetPassword extends GenericResponse{

    private final String password;

    public SetPassword(String nickname, String password) {
        super("Password set! Waiting for other players to join the lobby...", nickname);
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
}
