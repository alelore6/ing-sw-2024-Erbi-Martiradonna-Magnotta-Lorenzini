package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Controller.Severity;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.*;
import it.polimi.ingsw.Listeners.ModelViewListener;
import it.polimi.ingsw.ServerApp;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server{

    private final ServerApp serverApp;
    private volatile int endSent = 0;
    public static final int PING_INTERVAL = 3000; // milliseconds
    public final Controller controller = new Controller(this);
    public final Logger logger;

    // This lock allows to serialize the incoming events from possible multiple clients, e.g:
    // if two clients connect at almost the same time, only one (the first) will have the NumPlayerRequest event.
    private Object lock_update  = new Object();
    private Object lock_end     = new Object();

    private final HashMap<String, Client> clients = new HashMap<>();
    public final List<String> disconnectedClients = new ArrayList<>();

    //server constructor with the default rmi port
    public ServerImpl(ServerApp serverApp, Logger logger) throws RemoteException {
        super();
        this.serverApp = serverApp;
        this.logger = logger;
        pong();
    }

    //server implementation with a certain RMI port
    public ServerImpl(ServerApp serverApp, Logger logger, int port) throws RemoteException {
        super(port);
        this.serverApp = serverApp;
        this.logger = logger;
        pong();
    }

    public void ping(){}

    public void register(Client client) throws RemoteException{
        boolean isReconnected = false;

        synchronized(clients){
            // Initialize of a client
            String oldNickname = client.getNickname();

            String temp = oldNickname;

            while(findClientByNickname(temp, client) != null){
                // An identical nickname has been found and adds a sequential number at the end of the nickname.
                temp = temp + "." + clients.size();
            }

            synchronized(disconnectedClients){
                if(disconnectedClients.contains(client.getNickname()))
                    isReconnected = true;
                else
                    clients.put(temp, client);
            }

            // if MALEDETTO (RIP)
            if(!(client instanceof ClientSkeleton) && !isReconnected)
                System.out.println(temp + " is connected with RMI.");

            ModelViewListener listener;

            synchronized(disconnectedClients){
                listener = new ModelViewListener(this, client);

                // This starts the handle event
                controller.addMVListener(listener);

                if(isReconnected) listener.addEvent(new ReconnectionRequest(client.getNickname()));
                else{

                    client.setNickname(temp);
                    clients.replace(temp, client);
                    listener.nickname = temp;

                    controller.addPlayerToLobby(temp, listener, oldNickname);
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
                        for (String nickname : clients.keySet()) {
                            try {
                                clients.get(nickname).ping();
                            } catch (RemoteException e) {
                                System.err.println(nickname + " is disconnected.");

                                disconnectedClients.add(nickname);

                                ModelViewListener listener = controller.getMVListenerByNickname(nickname);

                                listener.stop();
                                controller.getMVListeners().remove(listener);
                                clients.remove(nickname);
                                controller.disconnectPlayer(nickname);
                            }
                        }
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
                for(String nick : clients.keySet()){
                    if(!clients.get(nick).equals(clientToExclude) && clients.get(nick).getNickname() != null && clients.get(nick).getNickname().equals(nickname))
                        return clients.get(nick);
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

    public HashMap<String, Client> getClients(){
        return clients;
    }

    public void notifyEndSent(){
        synchronized (lock_end){
            endSent++;
        }
    }

    public synchronized void restart(){

        boolean allSent = false;

        while(true){
            if(endSent >= clients.size()) break;
        }

        System.exit(0);

        // TODO: se avanza tempo (lavoro già iniziato).
        // serverApp.restart();
        // return;
    }
}
