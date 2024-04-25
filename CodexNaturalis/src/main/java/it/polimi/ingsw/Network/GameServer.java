package it.polimi.ingsw.Network;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    public Controller controller = new Controller(this);
    private static final List<Client> clientList = new ArrayList<>();
    private static int numClient = 0;

    public boolean addClient(Client client){
        // controllo nickname
        boolean ok=false;
        try{
            findClient(client.nickname);
        }catch (RuntimeException e){
            ok=true;
        }
        if(ok) {
            //lo aggiungo
            clientList.add(client);
            numClient++;
            controller.addPlayerToLobby(client.nickname);
            return true;
        } else return false;
    }
    public Client findClient(String nickname){
        for (Client c : clientList){
            if (c.nickname.equalsIgnoreCase(nickname))
                return c;
        }
        // client not found
        throw new RuntimeException("client "+nickname+" not found");
    }
}
