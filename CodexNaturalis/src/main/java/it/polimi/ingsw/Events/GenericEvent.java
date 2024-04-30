package it.polimi.ingsw.Events;

import java.io.Serializable;

public abstract class GenericEvent implements Serializable {
    public String message;
    public final String nickname;
    public GenericEvent(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }
}
