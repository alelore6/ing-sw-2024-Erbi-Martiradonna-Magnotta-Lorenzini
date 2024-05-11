package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.ClientRegister;
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
        if(client instanceof ClientImpl){ // if MALEDETTO
            try{
                // check nickname
                findClient(((ClientImpl) client).getNickname());
            }catch (RuntimeException e){
                // TODO: crea evento che setta il nuovo nick all'user

                //add a sequential number at the end of the nickname if already present
                ((ClientImpl) client).setNickname(((ClientImpl) client).getNickname() + numClient);
            }
            CLIENT_IMPL_LIST.add((ClientImpl) client);
            numClient++;
            controller.addPlayerToLobby(((ClientImpl) client).getNickname());
        }
        else clientSkeleton = (ClientSkeleton) client;
    }

    public void findClient(String nickname){
        for (ClientImpl c : CLIENT_IMPL_LIST){
            if (c.getNickname().equalsIgnoreCase(nickname))
                // client not found
                throw new RuntimeException("client " + nickname + " not found");
        }
    }

    @Override
    public void update(Client client, GenericEvent event) throws RemoteException{
        if(event instanceof ClientRegister){
            controller.updateModel(event, event.nickname);
        }else {
            //client has responded to a request to modify the model
            for (int i = 0; i < CLIENT_IMPL_LIST.size(); i++) {
                if (CLIENT_IMPL_LIST.get(i).getNickname().equalsIgnoreCase(((ClientImpl) client).getNickname())) {
                    controller.updateModel(event, CLIENT_IMPL_LIST.get(i).getNickname());
                }
            }
        }
//        if(CLIENT_IMPL_LIST.contains(Clien)){
//            //check it is client's turn
//            if(((ClientImpl) client).getNickname().equalsIgnoreCase( controller.getGame().getCurrentPlayer())) {
//                controller.updateModel(event,((ClientImpl) client).getNickname());
//            }
//            else throw new RemoteException("It is not "+((ClientImpl)client).getNickname() +" turn to play");
//        } else throw new RemoteException("Client sending the event isn't registered to the server");
    }

    public void sendEvent(GenericEvent event) throws RemoteException {
        if (clientSkeleton!=null)
            clientSkeleton.update(event);
        else throw new RemoteException("Cannot send event: clientSkeleton not found.\n");
    }
}
