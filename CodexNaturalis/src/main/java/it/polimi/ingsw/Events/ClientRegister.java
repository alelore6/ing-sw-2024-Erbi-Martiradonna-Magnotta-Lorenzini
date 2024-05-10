package it.polimi.ingsw.Events;

import it.polimi.ingsw.Distributed.ClientImpl;

public class ClientRegister extends GenericEvent{

    private ClientImpl client;

    public ClientRegister(ClientImpl client) {
        super("connessione..", client.getNickname());
        this.client = client;
        System.out.println("SIAMO QUI!");
    }

    public ClientImpl getClient() {
        return client;
    }
}
