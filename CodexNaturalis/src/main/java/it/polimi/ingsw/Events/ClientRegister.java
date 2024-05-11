package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.View.UI;

import java.io.Serializable;

public class ClientRegister extends GenericEvent {
    private String nickname;

    public ClientRegister(ClientImpl client) {
        super("connessione..", client.getNickname());
        this.nickname = client.getNickname();
        System.out.println("SIAMO QUI!");
    }

    public String getNickname() {
        return this.nickname;
    }
}
