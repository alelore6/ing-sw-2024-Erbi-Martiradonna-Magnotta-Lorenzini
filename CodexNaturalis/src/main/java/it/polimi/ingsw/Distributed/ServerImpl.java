package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.controller.Controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server{
    public Controller controller = new Controller(this);
    private static final List<ClientImpl> CLIENT_IMPL_LIST = new ArrayList<>();
    private static int numClient = 0;

    //server constructor with the default rmi port
    protected ServerImpl() throws RemoteException {
        super();
        //need to bind the controller to the model, something like controller.setgame(model)
    }

    //server implementation with a certain RMI port
    protected ServerImpl(int port) throws RemoteException {
        super(port);
        //need to bind the controller to the model, something like controller.setgame(model)
    }


    @Override
    public boolean addClient(Client client) throws RemoteException{
        // controllo nickname
        boolean ok=false;
        try{
            findClient(ClientImpl.nickname);
        }catch (RuntimeException e){
            ok=true;
        }
        if(ok) {
            //lo aggiungo
            CLIENT_IMPL_LIST.add(ClientImpl);
            numClient++;
            controller.addPlayerToLobby(ClientImpl.nickname);
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
    public void update(Client client, String event) throws RemoteException {
        //TODO implement
    }
}
