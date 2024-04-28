package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Messages.Events;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientSkeleton implements Client{

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public ClientSkeleton(Socket socket)throws RemoteException {
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
    public void update(Events e) throws RemoteException {
        try {
            out.writeObject(e);
        } catch (IOException ex) {
            throw new RemoteException("Cannot send to client.");
        }
        //TODO listener?
    }


    public void receive(ServerImpl server) throws RemoteException {
        Events event;
        String arg;
        try {
            event =(Events) in.readObject();
            arg = (String) in.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize from client", e);
        }
        server.update(this, event, arg);
    }
}
