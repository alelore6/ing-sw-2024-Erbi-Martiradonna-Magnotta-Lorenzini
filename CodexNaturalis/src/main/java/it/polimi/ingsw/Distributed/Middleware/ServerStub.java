package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;

public class ServerStub implements Server, Serializable {

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
        try{
            this.socket = new Socket(ip, port);
            try{
                this.out = new ObjectOutputStream(socket.getOutputStream());
            }catch(IOException e) {
                throw new RemoteException("Error in creating output stream", e);
            }
            try {
                this.in = new ObjectInputStream(socket.getInputStream());
            }catch(IOException e) {
                throw new RemoteException("Error in creating input stream", e);
            }
        }catch(IOException e) {
            throw new RemoteException("Can't connect to the server", e);
        }

        System.out.println("Connected!");
    }

    @Override
    public void update(Client client, GenericEvent event) throws RemoteException {
        try {
            System.out.println("Sending " + event.msgOutput() + " to server");
            out.writeObject(event);
            out.flush();
            System.out.println("Event: " + event.msgOutput() + " sent to server");
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }
        //socket: client skeleton is always reading
    }

    public GenericEvent receive(Client client) throws RemoteException {
        //socket: receive from client skeleton update()

        GenericEvent ev = null;
        try{
            System.out.println("Waiting to receive event from server...");
            ev = (GenericEvent) in.readObject();
            System.out.println("Received: " + ev.msgOutput() + " from server");
        }catch(IOException e){
            throw new RemoteException("Can't receive event from client", e);
        }catch(ClassNotFoundException e){
            throw new RemoteException("Can't deserialize event from client", e);
        }

        return ev;
    }

    public void close() throws RemoteException{
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close socket", e);
        }
    }
}
