package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ModelViewListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerImpl extends UnicastRemoteObject implements Server{

    public  final Controller controller = new Controller(this);

    private final ArrayList<ClientSkeleton> clientSkeletons = new ArrayList<ClientSkeleton>();
    private final ArrayList<Client> clientProxies = new ArrayList<>();

    private int numClient = 0;
    public final Logger logger;

    //server constructor with the default rmi port
    public ServerImpl(Logger logger) throws RemoteException {
        super();
        this.logger = logger;
    }

    //server implementation with a certain RMI port
    public ServerImpl(Logger logger, int port) throws RemoteException {
        super(port);
        this.logger = logger;
    }

    @Override
    public void register(Client client) throws RemoteException{
        numClient++;
        controller.addMVListener(new ModelViewListener(this, client));

        // if MALEDETTO
        if(client instanceof ClientSkeleton)    clientSkeletons.add((ClientSkeleton) client);
        else                                    clientProxies.add(client);
    }

    public Client findClientByNickname(String nickname) throws RemoteException {
        Client client = findCPByNickname(nickname);
        if(client != null)  return client;
        client = findCSbyNickname(nickname);
        if(client != null)  return client;

        return null;
    }

    // TODO: this must be well synchronized.
    public Client findCPByNickname(String nickname) throws RemoteException {
        for(Client client : clientProxies){
            if (client.getNickname() != null && client.getNickname().equalsIgnoreCase(nickname))    return client;
        }

        return null;
    }

    public ClientSkeleton findCSbyNickname(String nickname){
        for(ClientSkeleton client : clientSkeletons){
            if(client.getNickname() != null && client.getNickname().equalsIgnoreCase(nickname))     return client;
        }

        return null;
    }

    @Override
    public void update(Client client, GenericEvent event) throws RemoteException{
        if(event instanceof ClientRegister){
            register(client);
            setClient(client, (ClientRegister) event);
        }
        else {
            //client has responded to a request to modify the model
            for(Client c : clientProxies){
                if (c.getNickname().equalsIgnoreCase(client.getNickname())) {
                    controller.updateModel(event, c.getNickname());
                }
            }
            for(ClientSkeleton c : clientSkeletons){
                if(c.getNickname().equalsIgnoreCase(client.getNickname())){
                    controller.updateModel(event, c.getNickname());
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

    private void setClient(Client client, ClientRegister event) throws RemoteException {
        String oldNickname = event.getNickname();

        Client temp = findClientByNickname(oldNickname);
        // An identical nickname has been found and adds a sequential number at the end of the nickname.
        if(temp != null && !temp.equals(client))    client.setNickname(oldNickname + numClient);
        else                                        client.setNickname(oldNickname);

        controller.addPlayerToLobby(client.getNickname(), controller.getMVListenerByNickname(client.getNickname()), oldNickname);
    }

    public void sendEventToAll(GenericEvent event) throws RemoteException {
        for(ClientSkeleton client : clientSkeletons) sendEvent(client, event);
        for(Client         client : clientProxies)   sendEvent(client, event);
    }

    public void sendEvent(Client client, GenericEvent event) throws RemoteException {
        client.update(event);
    }

    public ArrayList<Client> getClientProxies() {
        return clientProxies;
    }
}
