package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Events.ReconnectionRequest;
import it.polimi.ingsw.Events.ReconnectionResponse;
import it.polimi.ingsw.Listeners.ModelViewListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerImpl extends UnicastRemoteObject implements Server{

    private static final int PING_INTERVAL = 5000; // milliseconds
    public final Controller controller = new Controller(this);
    public final Logger logger;

    // This lock allows to serialize the incoming events from possible multiple clients, e.g:
    // if two clients connect at almost the same time, only one (the first) will have the NumPlayerRequest event.
    private Object lock_update  = new Object();

    private int numClient = 0;
    private final HashMap<Client, String> clients = new HashMap<>();
    public final HashMap<Client, String> disconnectedClients = new HashMap<>();

    //server constructor with the default rmi port
    public ServerImpl(Logger logger) throws RemoteException {
        super();
        this.logger = logger;
        pong();
    }

    //server implementation with a certain RMI port
    public ServerImpl(Logger logger, int port) throws RemoteException {
        super(port);
        this.logger = logger;
        pong();
    }

    @Override
    public void register(Client client) throws RemoteException{
        synchronized(clients){
            synchronized(disconnectedClients){
                if(disconnectedClients.containsKey(client)){
                    for(Client c : disconnectedClients.keySet()){
                        //if(controller.getMVListenerByNickname(disconnectedClients.get(c)))
                    }
                }
                else{
                    clients.put(client, client.getNickname());
                    numClient++;
                }
            }

            // if MALEDETTO (RIP)
            if(!(client instanceof ClientSkeleton))
                System.out.println(client.getNickname() + " is connected with RMI.");
        }
    }

    private void pong(){
        // Thread for periodic controls.
        new Thread(() -> {
            while (true) {
                String residue = null;

                synchronized (clients){
                    synchronized(disconnectedClients){
                        for (Client client : clients.keySet()) {
                            try {
                                client.ping();
                            } catch (RemoteException e) {
                                System.err.println("A client is disconnected.");

                                disconnectedClients.put(client, clients.get(client));

                                for(ModelViewListener listener : controller.getMVListeners()){
                                    if(listener.client.equals(client)){
                                        controller.disconnectPlayer(listener.nickname);
                                        controller.getMVListeners().remove(listener);
                                        break;
                                    }
                                }
                            }
                        }

                        clients.keySet().removeAll(disconnectedClients.keySet());
                    }
                }

                try {
                    Thread.sleep(PING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Client findClientByNickname(String nickname, Client clientToExclude){
        synchronized (clients){
            try{
                for(Client client : clients.keySet()){
                    if(!client.equals(clientToExclude) && client.getNickname() != null && client.getNickname().equals(nickname))
                        return client;
                }
            }catch(RemoteException e){}
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
                if(event instanceof ReconnectionResponse)
                    ((ReconnectionResponse) event).client = client;

                //client has responded to a request to modify the model
                for(Client c : clients.keySet()){
                    if (c.getNickname().equals(client.getNickname()))
                        controller.updateModel(event, c.getNickname());
                }
            }
        }
    }

    private void setClient(Client client, ClientRegister event) throws RemoteException{
        String oldNickname = event.getNickname();
        ModelViewListener listener;

        synchronized (clients){
            synchronized(disconnectedClients){
                listener = new ModelViewListener(this, client);

                // This starts the handle event
                controller.addMVListener(listener);

                if(disconnectedClients.containsKey(client))
                    listener.addEvent(new ReconnectionRequest(client.getNickname()));
                else{   // Initialize of a client
                    Client temp = findClientByNickname(oldNickname, client);
                    // An identical nickname has been found and adds a sequential number at the end of the nickname.
                    if(temp != null && !temp.equals(client))    client.setNickname(oldNickname + "." + numClient);
                    else                                        client.setNickname(oldNickname);

                    controller.addPlayerToLobby(client.getNickname(), listener, oldNickname);
                }
            }
        }
    }

    public HashMap<Client, String> getClients(){
        return clients;
    }
}
