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

public class ClientSkeleton implements Client {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Logger logger;
    public final Socket socket;
    private ServerImpl server;

    private String nickname = null;

    /**
     * Constructor
     *
     * @param socket
     * @param logger
     * @throws RemoteException
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
     * @throws RemoteException
     */
    @Override
    public void ping() throws RemoteException {}

    /**
     * Method to update the client with an event using a socket type connection.
     * @param event
     * @throws RemoteException
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
     * @param server
     * @throws RemoteException
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
     * @param nickname
     */
    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for the nickname.
     * @return
     */
    @Override
    public String getNickname() {
        return nickname;
    }
}
