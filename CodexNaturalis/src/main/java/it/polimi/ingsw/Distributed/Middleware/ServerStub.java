package it.polimi.ingsw.Distributed.Middleware;

import it.polimi.ingsw.Distributed.Client;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Events.ReadObject;
import it.polimi.ingsw.View.View;

import java.io.IOException;
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
    }

    @Override
    public void update(Client client, GenericEvent event) throws RemoteException {
        try {
            out.writeObject(event);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event", e);
        }

    }

    public void receive(Client client) throws RemoteException {
        View view;

        try{
            view = (View)in.readObject();
        }catch(IOException e){
            throw new RemoteException("Can't get model view from client", e);
        }catch (ClassNotFoundException e){
            throw new RemoteException("Can't deserialize model view from the client", e);
        }

        ReadObject ev;
        try{
            ev = (ReadObject) in.readObject();
        }catch(IOException e){
            throw new RemoteException("Can't receive event from client", e);
        }catch(ClassNotFoundException e){
            throw new RemoteException("Can't deserialize event from client", e);
        }

        client.update(view, ev);
    }

    public void close() throws RemoteException{
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't close socket", e);
        }
    }
}
