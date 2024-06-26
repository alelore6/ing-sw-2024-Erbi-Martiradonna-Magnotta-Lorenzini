package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Events.GenericEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Class representing the server in socket connections.
 */
public class ServerStub implements Server, Serializable {

    /**
     * The IP address of the server
     */
    private final String ip;
    /**
     * The port on which the server accepts connections from clients.
     */
    private final int port;
    /**
     * The socket class needed for the connection.
     */
    private Socket socket;

    /**
     * The stream to write objects to the clients.
     */
    private ObjectOutputStream out;
    /**
     * The stream to receive objects from the clients.
     */
    private ObjectInputStream in;

    /**
     * Constructor Initializes the IP and port attributes.
     * @param ip IP address of the server
     * @param port Port number of the server on which the clients will connect.
     */
    public ServerStub(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Method to register a socket type connection with the server.
     * @param client the client to register a connection with.
     * @throws RemoteException exception thrown in remote connections.
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
     * Method to update a client with an event using a socket type connection.
     * @param client the client to which the event is sent to.
     * @param event the event (information) which the server sends to the client.
     * @throws RemoteException exception thrown in remote connections.
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
     * @return the generic event object read from the object input stream.
     * @throws RemoteException exception thrown in remote connections.
     */
    public GenericEvent receive() throws RemoteException {
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
     * @throws RemoteException exception thrown in remote connections.
     */
    public void close() throws RemoteException{
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close socket", e);
        }
    }
}
