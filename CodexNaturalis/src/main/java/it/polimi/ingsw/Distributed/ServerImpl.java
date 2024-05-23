package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ModelViewListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ServerImpl extends UnicastRemoteObject implements Server{

    public  final Controller controller = new Controller(this);

    public final Logger logger;

    // This lock allows to serialize the incoming events from possible multiple clients, e.g:
    // if two clients connect at almost the same time, only one (the first) will have the NumPlayerRequest event.
    private Object lock_update  = new Object();

    private Object lock_clients = new Object();
    private int numClient = 0;
    private final ArrayList<Client> clientProxies = new ArrayList<>();
    private final ArrayList<ClientSkeleton> clientSkeletons = new ArrayList<ClientSkeleton>();

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
        controller.addMVListener(new ModelViewListener(this, client));

        synchronized(lock_clients){
            numClient++;

            // if MALEDETTO
            if(client instanceof ClientSkeleton)    clientSkeletons.add((ClientSkeleton) client);
            else                                    clientProxies.add(client);
        }
    }

    public Client findClientByNickname(String nickname) throws RemoteException {
        Client client = findCPByNickname(nickname);
        if(client != null)  return client;
        client = findCSbyNickname(nickname);
        if(client != null)  return client;

        return null;
    }

    public Client findCPByNickname(String nickname) throws RemoteException {
        synchronized (lock_clients){
            for(Client client : clientProxies){
                if (client.getNickname() != null && client.getNickname().equalsIgnoreCase(nickname))    return client;
            }
        }

        return null;
    }

    public ClientSkeleton findCSbyNickname(String nickname){
        synchronized (lock_clients){
            for(ClientSkeleton client : clientSkeletons){
                if(client.getNickname() != null && client.getNickname().equalsIgnoreCase(nickname))     return client;
            }
        }

        return null;
    }

    @Override
    public void update(Client client, GenericEvent event) throws RemoteException{
        // If client is connected with RMI.
        if(!(client instanceof ClientSkeleton)) logger.addLog(event, Severity.RECEIVED);
        synchronized(lock_update){
            if(event instanceof ClientRegister){
                register(client);
                setClient(client, (ClientRegister) event);
            }
            else{
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
        }
//        if(CLIENT_IMPL_LIST.contains(Clien)){
//            //check it is client's turn
//            if(((ClientImpl) client).getNickname().equalsIgnoreCase( controller.getGame().getCurrentPlayer())) {
//                controller.updateModel(event,((ClientImpl) client).getNickname());
//            }
//            else throw new RemoteException("It is not "+((ClientImpl)client).getNickname() +" turn to play");
//        } else throw new RemoteException("Client sending the event isn't registered to the server");
    }

    private void setClient(Client client, ClientRegister event) throws RemoteException{
        String oldNickname = event.getNickname();

        synchronized (lock_clients){
            Client temp = findClientByNickname(oldNickname);
            // An identical nickname has been found and adds a sequential number at the end of the nickname.
            if(temp != null && !temp.equals(client))    client.setNickname(oldNickname + numClient);
            else                                        client.setNickname(oldNickname);

            controller.addPlayerToLobby(client.getNickname(), controller.getMVListenerByNickname(client.getNickname()), oldNickname);
        }
    }

    public ArrayList<Client> getClientProxies(){
        return clientProxies;
    }

    public ArrayList<ClientSkeleton> getClientSkeletons(){
        return clientSkeletons;
    }
}
