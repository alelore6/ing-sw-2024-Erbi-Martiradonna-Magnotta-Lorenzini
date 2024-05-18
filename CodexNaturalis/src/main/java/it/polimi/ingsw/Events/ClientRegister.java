package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.View.UI;

import java.io.Serializable;

public class ClientRegister extends GenericEvent {


    public ClientRegister(ClientImpl client) {
        super("Connessione...", client.getNickname());
    }

    public String getNickname() {
        return this.nickname;
    }
}
