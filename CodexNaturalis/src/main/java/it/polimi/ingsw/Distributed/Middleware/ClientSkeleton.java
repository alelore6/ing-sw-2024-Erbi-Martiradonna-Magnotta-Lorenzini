package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.ServerImpl;
import it.polimi.ingsw.Events.FinalRankings;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.View;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * Class representing the client for socket connections. It's constructor differentiates from the class ClientImpl because of
 * the "socket" attribute.
 */
public class ClientSkeleton implements Client {

    /**
     * The object output stream where the client writes to the server.
     */
    private ObjectOutputStream out;
    /**
     * The object input stream where the client receives from the server
     */
    private ObjectInputStream in;
    /**
     * The logger class needed to "log" every action that goes through the network
     */
    private final Logger logger;
    /**
     * The end point for the communication between client and server
     */
    public final Socket socket;
    /**
     * The server from which the client receives information
     */
    private ServerImpl server;

    /**
     * The nickname associated with the client
     */
    private String nickname = null;

    /**
     * Constructor. Creates the object input and the object output streams and initializes the class.
     * @param socket the server socket needed for the communication between client and server.
     * @param logger the logger class of the client.
     * @throws RemoteException Exception thrown by remote connections
     */
    public ClientSkeleton(Socket socket, Logger logger) throws RemoteException {
        this.logger = logger;
        this.socket = socket;

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

    /**
     * Method to pong with the server.
     * @throws RemoteException exception thrown in remote connections.
     */
    @Override
    public void ping() throws RemoteException {}

    /**
     * Method to update the client with an event using a socket type connection.
     * @param event an event received by the server which the client needs to unmarshall and elaborate.
     * @throws RemoteException exception thrown in remote connections.
     */
    @Override
    public synchronized void update(GenericEvent event) throws RemoteException {
        try {
            logger.addLog(event, Severity.SENDING);
            out.reset();
            out.writeObject(event);
            out.flush();
            logger.addLog(event, Severity.SENT);
        } catch (IOException ex) {
            throw new RemoteException("Cannot send " + event.getClass().getName() + " to client");
        }
        //socket: server stub is always reading (same as receive() here)
    }

    /**
     * Method to update the server with an event coming from a socket type connection.
     * @param server the server to which the client will send the specified event.
     * @throws RemoteException exception thrown in remote connections.
     */
    public void receive(ServerImpl server) throws RemoteException {

        // Saves the server just in case (see the notifyEndSent() call above).
        this.server = server;

        //socket: receive from server stub update()
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

    /**
     * Setter for the nickname.
     * @param nickname the nickname attribute.
     */
    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for the nickname.
     * @return the nickname attribute.
     */
    @Override
    public String getNickname() {
        return nickname;
    }
}
