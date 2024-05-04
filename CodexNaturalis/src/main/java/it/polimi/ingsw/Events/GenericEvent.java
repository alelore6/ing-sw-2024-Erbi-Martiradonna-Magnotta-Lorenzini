package it.polimi.ingsw.Events;

import java.io.Serializable;

public abstract class GenericEvent implements Serializable {
    protected String message;
    protected final String nickname;
    public GenericEvent(String message, String nickname) {
        this.message = message;
        this.nickname = nickname;
    }

    public abstract String msgOutput();
}
