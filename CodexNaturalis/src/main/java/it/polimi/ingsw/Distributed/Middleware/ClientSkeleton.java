package it.polimi.ingsw.Distributed.Middleware;

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

    public String nickname = null;

    public ClientSkeleton(Socket socket) throws RemoteException {
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
            System.out.println("Sending  "+e.msgOutput()+"  to client");
            out.writeObject(e);
            out.flush();
            System.out.println("Event "+e.msgOutput()+"  sent to client");
        } catch (IOException ex) {
            throw new RemoteException("Cannot send to client.");
        }
        //socket: server stub is always reading (same as receive() here)
    }

    @Override
    public String getNickname() {
        return nickname;
    }


    public void receive(ServerImpl server) throws RemoteException {
        //socket: receive from server stub update()
        //not sure of the type
        GenericEvent event;
        try {
            System.out.println("Waiting to receive event from client...");
            event = (GenericEvent) in.readObject();
            System.out.println("Received event from client.");

        } catch (IOException e) {
            throw new RemoteException("Cannot receive from client", e);
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot deserialize from client", e);
        }
        server.update(this, event);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
