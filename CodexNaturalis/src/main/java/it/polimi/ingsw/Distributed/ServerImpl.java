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
import java.util.HashMap;

public class ServerImpl extends UnicastRemoteObject implements Server{

    private static final int PING_INTERVAL = 5000; // milliseconds
    public final Controller controller = new Controller(this);
    public final Logger logger;

    // This lock allows to serialize the incoming events from possible multiple clients, e.g:
    // if two clients connect at almost the same time, only one (the first) will have the NumPlayerRequest event.
    private Object lock_update  = new Object();

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

    public void register(Client client) throws RemoteException{
        boolean isReconnected = false;

        synchronized(clients){
            synchronized(disconnectedClients){
                if(disconnectedClients.containsValue(client.getNickname()))
                    isReconnected = true;
                else
                    clients.put(client, client.getNickname());
            }

            // if MALEDETTO (RIP)
            if(!(client instanceof ClientSkeleton) && !isReconnected)
                System.out.println(client.getNickname() + " is connected with RMI.");
        }

        ModelViewListener listener;

        synchronized (clients){
            synchronized(disconnectedClients){
                listener = new ModelViewListener(this, client);

                // This starts the handle event
                controller.addMVListener(listener);

                if(isReconnected) listener.addEvent(new ReconnectionRequest(client.getNickname()));
                else{
                    // Initialize of a client
                    String oldNickname = client.getNickname();
                    Client temp = findClientByNickname(oldNickname, client);
                    // An identical nickname has been found and adds a sequential number at the end of the nickname.
                    if(temp != null)    client.setNickname(oldNickname + "." + clients.size());

                    controller.addPlayerToLobby(client.getNickname(), listener, oldNickname);
                }
            }
        }
    }

    private void pong(){
        // Thread for periodic controls.
        new Thread(() -> {
            while (true) {
                synchronized (clients){
                    synchronized(disconnectedClients){
                        for (Client client : clients.keySet()) {
                            try {
                                client.ping();
                            } catch (RemoteException e) {
                                System.err.println(clients.get(client) + " is disconnected.");

                                disconnectedClients.put(client, clients.get(client));

                                for(ModelViewListener listener : controller.getMVListeners()){
                                    if(listener.client.equals(client)){
                                        controller.disconnectPlayer(clients.get(client));
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
                client.setNickname(((ClientRegister) event).getNickname());
                register(client);
            }
            else{
                if(event instanceof ReconnectionResponse)
                    ((ReconnectionResponse) event).client = client;

                //client has responded to a request to modify the model
                controller.updateModel(event, client.getNickname());
            }
        }
    }

    public HashMap<Client, String> getClients(){
        return clients;
    }
}
