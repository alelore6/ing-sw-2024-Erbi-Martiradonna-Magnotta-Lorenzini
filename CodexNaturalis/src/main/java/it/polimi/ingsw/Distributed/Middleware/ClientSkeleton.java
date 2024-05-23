package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.View;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSkeleton implements Client {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Logger logger;

    private String nickname = null;

    public ClientSkeleton(Socket socket, Logger logger) throws RemoteException {
        this.logger = logger;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create output stream", e);
        }
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create input stream", e);
        }
    }

    @Override
    public void update(GenericEvent e) throws RemoteException {
        try {
            logger.addLog(e, Severity.SENDING);
            out.writeObject(e);
            out.flush();
            logger.addLog(e, Severity.SENT);
        } catch (IOException ex) {
            throw new RemoteException("Cannot send " + e.getClass() +  " to client");
        }
        //socket: server stub is always reading (same as receive() here)
    }

    public void receive(ServerImpl server) throws RemoteException {
        //socket: receive from server stub update()
        //not sure of the type
        GenericEvent event;
        try {
            logger.addLog((GenericEvent) null, Severity.RECEIVING);
            event = (GenericEvent) in.readObject();
            logger.addLog(event, Severity.RECEIVED);

        } catch (IOException e) {
            throw new RemoteException("Cannot receive from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize from client", e);
        }
        server.update(this, event);
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getNickname() {
        return nickname;
    }
}
