package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.View.UI;

import java.io.Serializable;

public class ClientRegister extends GenericEvent {
    private static final long serialVersionUID = 5L;
    private String nickname;

    public ClientRegister(ClientImpl client) {
        super("Connessione...", client.getNickname());
        this.nickname = client.getNickname();
    }

    public String getNickname() {
        return this.nickname;
    }
}
