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
    }

    //server implementation with a certain RMI port
    protected ServerImpl(int port) throws RemoteException {
        super(port);
    }


    @Override
    public void register(Client client) throws RemoteException{
        // check nickname
        try{
            findClient(((ClientImpl) client).nickname);
        }catch (RuntimeException e){
            //add a sequential number at the end of the nickname
            ((ClientImpl) client).nickname = ((ClientImpl) client).nickname + numClient;
        }
        CLIENT_IMPL_LIST.add((ClientImpl) client);
        numClient++;
        controller.addPlayerToLobby(((ClientImpl) client).nickname);
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
