package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.GenericEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server{
    public Controller controller = new Controller(this);
    private static final List<ClientImpl> CLIENT_IMPL_LIST = new ArrayList<>();
    private static int numClient = 0;
    private ClientSkeleton clientSkeleton;

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
        if(client instanceof ClientImpl){
            System.out.println("CIAO1234");
            try{
                // check nickname
                findClient(((ClientImpl) client).getNickname());
            }catch (RuntimeException e){
                //add a sequential number at the end of the nickname if already present
                ((ClientImpl) client).setNickname(((ClientImpl) client).getNickname() + numClient);
            }
            CLIENT_IMPL_LIST.add((ClientImpl) client);
            numClient++;
            controller.addPlayerToLobby(((ClientImpl) client).getNickname());
        }
        else clientSkeleton = (ClientSkeleton) client;
    }

    public ClientImpl findClient(String nickname){
        for (ClientImpl c : CLIENT_IMPL_LIST){
            if (c.getNickname().equalsIgnoreCase(nickname))
                return c;
        }
        // client not found
        throw new RuntimeException("client "+nickname+" not found");
    }


    @Override
    public void update(Client client, GenericEvent event) throws RemoteException {
        //client has responded to a request to modify the model
        if(CLIENT_IMPL_LIST.contains(((ClientImpl) client))){
            //check it is client's turn
            if(((ClientImpl) client).getNickname().equalsIgnoreCase( controller.getGame().getCurrentPlayer())) {
                controller.updateModel(event,((ClientImpl) client).getNickname());
            }
            else throw new RemoteException("It is not "+((ClientImpl)client).getNickname() +" turn to play");
        } else throw new RemoteException("Client sending the event isn't registered to the server");

    }

    public void sendEvent(GenericEvent event) throws RemoteException {
        if (clientSkeleton!=null)
            clientSkeleton.update(findClient(event.nickname),event);
        else throw new RemoteException("Cannot send event: clientSkeleton not found.\n");
    }
}
