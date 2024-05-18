package it.polimi.ingsw.Events;

public class SetPassword extends GenericEvent{

    private static final long serialVersionUID = 20L;

    private final String password;

    public SetPassword(String nickname, String password) {
        super(nickname + "'s password set!", nickname);
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
}
