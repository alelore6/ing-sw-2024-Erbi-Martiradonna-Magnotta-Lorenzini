package it.polimi.ingsw.Exceptions;

public class PlayerNotFoundException extends Throwable {

    public String nickname;

    public PlayerNotFoundException(String nickname) {
        this.nickname = nickname;
    }
}
