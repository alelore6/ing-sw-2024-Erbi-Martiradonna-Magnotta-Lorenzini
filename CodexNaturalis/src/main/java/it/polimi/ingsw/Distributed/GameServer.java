package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends UnicastRemoteObject implements Server{
    public Controller controller = new Controller(this);
    private static final List<Client> clientList = new ArrayList<>();
    private static int numClient = 0;

    //server constructor with the default rmi port
    protected GameServer() throws RemoteException {
        super();
        //need to bind the controller to the model, something like controller.setgame(model)
    }

    //server implementation with a certain RMI port
    protected GameServer(int port) throws RemoteException {
        super(port);
        //need to bind the controller to the model, something like controller.setgame(model)
    }



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
