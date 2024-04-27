package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Messages.Events;
import it.polimi.ingsw.Controller.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server{
    public Controller controller = new Controller(this);
    private static final List<ClientImpl> CLIENT_IMPL_LIST = new ArrayList<>();
    private static int numClient = 0;

    //server constructor with the default rmi port
    public ServerImpl() throws RemoteException {
        super();
        //need to bind the Controller to the model, something like Controller.setgame(model)
    }

    //server implementation with a certain RMI port
    protected ServerImpl(int port) throws RemoteException {
        super(port);
        //need to bind the Controller to the model, something like Controller.setgame(model)
    }


    @Override
    public boolean register(Client client) throws RemoteException{
        // controllo nickname
        boolean ok=false;
        try{
            findClient(((ClientImpl) client).nickname);
        }catch (RuntimeException e){
            ok=true;
        }
        if(ok) {
            //lo aggiungo
            CLIENT_IMPL_LIST.add((ClientImpl) client);
            numClient++;
            controller.addPlayerToLobby(((ClientImpl) client).nickname);
            return true;
        } else return false;
    }
    public ClientImpl findClient(String nickname){
        for (ClientImpl c : CLIENT_IMPL_LIST){
            if (c.nickname.equalsIgnoreCase(nickname))
                return c;
        }
        // client not found
        throw new RuntimeException("client "+nickname+" not found");
    }


    @Override
    public void update(Client client, Events event) throws RemoteException {
        //TODO implement
    }
}
