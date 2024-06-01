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

    private static final int PING_INTERVAL = 5000; // milliseconds
    public final Controller controller = new Controller(this);
    public final Logger logger;

    // This lock allows to serialize the incoming events from possible multiple clients, e.g:
    // if two clients connect at almost the same time, only one (the first) will have the NumPlayerRequest event.
    private Object lock_update  = new Object();

    private Object lock_clients = new Object();
    private int numClient = 0;
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Client> disconnectedClients = new ArrayList<>();

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
        synchronized(lock_clients){
            numClient++;

            clients.add(client);

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
                        for (Client client : clients) {
                            try {
                                client.ping();
                            } catch (RemoteException e) {
                                System.err.println("A client is disconnected.");

                                disconnectedClients.add(client);

                                for(ModelViewListener listener : controller.getMVListeners()){
                                    if(listener.client.equals(client)){
                                        controller.disconnectPlayer(listener.nickname);
                                        controller.getMVListeners().remove(listener);
                                        break;
                                    }
                                }
                            }
                        }

                        clients.removeAll(disconnectedClients);
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
        synchronized (lock_clients){
            try{
                for(Client client : clients){
                    if(!client.equals(clientToExclude) && client.getNickname() != null && client.getNickname().equalsIgnoreCase(nickname))
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
                if(!(client instanceof ClientSkeleton))
                    register(client);

                setClient(client, (ClientRegister) event);
            }
            else{
                //client has responded to a request to modify the model
                for(Client c : clients){
                    if (c.getNickname().equalsIgnoreCase(client.getNickname()))
                        controller.updateModel(event, c.getNickname());
                }
            }
        }
    }

    private void setClient(Client client, ClientRegister event) throws RemoteException{
        String oldNickname = event.getNickname();

        synchronized (lock_clients){
            Client temp = findClientByNickname(oldNickname, client);
            // An identical nickname has been found and adds a sequential number at the end of the nickname.
            if(temp != null && !temp.equals(client))    client.setNickname(oldNickname + "." + numClient);
            else                                        client.setNickname(oldNickname);

            ModelViewListener listener = new ModelViewListener(this, client);
            controller.addMVListener(listener);
            controller.addPlayerToLobby(client.getNickname(), listener, oldNickname);
        }
    }

    public ArrayList<Client> getClients(){
        return clients;
    }
}
