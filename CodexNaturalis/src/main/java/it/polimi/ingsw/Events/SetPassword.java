package it.polimi.ingsw.Events;

public class SetPassword extends GenericEvent{

    private final String password;

    public SetPassword(String nickname, String password) {
        super(nickname + "'s password set!", nickname);
        this.password = password;
    }
}
