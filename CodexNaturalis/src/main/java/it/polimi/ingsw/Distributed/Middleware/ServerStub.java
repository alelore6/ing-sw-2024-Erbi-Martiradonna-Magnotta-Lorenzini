package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Messages.Events;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerStub implements Server {

    private String ip;
    private int port;
    private Socket socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void register(Client client) throws RemoteException {
        //TODO implement
    }

    @Override
    public void update(Client client, Events event) throws RemoteException {
        //TODO implement
    }
}
