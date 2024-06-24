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

    private final String ip;
    private final int port;
    private Socket socket;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Constructor
     *
     * @param ip
     * @param port
     */
    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Method to register a socket type connection with the server.
     * @param client
     * @throws RemoteException
     */
    public void register(Client client) throws RemoteException {
        try{
            this.socket = new Socket(ip, port);
            socket.setKeepAlive(true);
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

    /**
     * Method to pong with the client.
     */
    public void ping(){}

    /**
     * Method to update the server with an event using a socket type connection.
     * @param client
     * @param event
     * @throws RemoteException
     */
    @Override
    public void update(Client client, GenericEvent event) throws RemoteException {
        try {
            // System.out.println("Sending " + event.msgOutput() + " to server");
            out.reset();
            out.writeObject(event);
            out.flush();
            // System.out.println("[DEBUG] Event sent to server: " + event.getClass().getName());
        } catch (IOException e) {
            System.out.println("Error in updating " + event.getClass());
            throw new RemoteException("Cannot send event to server.", e);
        }
        //socket: client skeleton is always reading
    }

    /**
     * Method to receive an event coming from a socket type connection.
     * @param client
     * @return
     * @throws RemoteException
     */
    public GenericEvent receive(Client client) throws RemoteException {
        //socket: receive from client skeleton update()

        GenericEvent ev = null;
        try{
            // System.out.println("Waiting to receive event from server...");
            ev = (GenericEvent) in.readObject();
            // System.out.println("Received: " + ev.msgOutput() + " from server");
        }catch(IOException e){
            throw new RemoteException("Can't receive event from client", e);
        }catch(ClassNotFoundException e){
            throw new RemoteException("Can't deserialize event from client", e);
        }

        return ev;
    }

    /**
     * Method to close the socket connection.
     * @throws RemoteException
     */
    public void close() throws RemoteException{
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close socket", e);
        }
    }
}
