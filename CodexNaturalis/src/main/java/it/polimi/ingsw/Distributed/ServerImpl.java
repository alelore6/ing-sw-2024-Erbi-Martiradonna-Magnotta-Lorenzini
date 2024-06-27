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

import static java.lang.Thread.sleep;

/**
 * Class representing a server instance.
 */
public class ServerImpl extends UnicastRemoteObject implements Server{

    /**
     * The server application.
     */
    private final ServerApp serverApp;

    /**
     * Attribute that counts how many FinalRankings the server has sent.
     */
    private volatile int endSent = 0;
    /**
     * List of players to be deleted from the main list.
     */
    private ArrayList<String> toDelete = new ArrayList<>();
    /**
     * Attribute representing how often the server pings the clients (in milliseconds).
     */
    public static final int PING_INTERVAL = 3000;
    /**
     * The controller of MVC pattern.
     */
    public final Controller controller = new Controller(this);
    /**
     * The logger.
     */
    public final Logger logger;

    /**
     * This lock allows serializing the incoming events from possible multiple clients, e.g.:
     * if two clients connect at almost the same time, only one (the first) will have the NumPlayerRequest event.
     */
    private Object lock_update = new Object();
    /**
     * This lock allows the synchronization of the endSent attribute incrementation.
     */
    private Object lock_end = new Object();

    /**
     * Hash map that links a nickname to its specific client instance. Notice that, in case of
     * a disconnection and then a rejoining, the particular instance changes.
     */
    private final HashMap<String, Client> clients = new HashMap<>();
    /**
     * List of nicknames that have been disconnected from the game.
     */
    public final List<String> disconnectedClients = new ArrayList<>();

    /**
     * Constructor with the default RMI port.
     * @param serverApp
     * @param logger
     * @throws RemoteException
     */
    public ServerImpl(ServerApp serverApp, Logger logger) throws RemoteException {
        super();
        this.serverApp = serverApp;
        this.logger = logger;
        pong();
    }

    /**
     * Constructor with a particular RMI port.
     * @param serverApp
     * @param logger
     * @param port
     * @throws RemoteException
     */
    public ServerImpl(ServerApp serverApp, Logger logger, int port) throws RemoteException {
        super(port);
        this.serverApp = serverApp;
        this.logger = logger;
        pong();
    }

    /**
     * Method to pong the RMI clients
     * @see it.polimi.ingsw.ClientApp
     */
    public void ping(){}

    /**
     * Method to register a client in the game.
     * @param client
     * @throws RemoteException
     */
    public void register(Client client) throws RemoteException{
        boolean isReconnected = false;

        synchronized(clients){
            // Initialize of a client
            String oldNickname = client.getNickname();

            String temp = oldNickname;

            while(clients.containsKey(temp)){
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

            synchronized(controller.getMVListeners()){
                // If the client is not rejoining, but it's joining for the first time
                if(controller.getMVListenerByNickname(client.getNickname()) == null)
                     listener = new ModelViewListener(this, client);
                else listener = controller.getMVListenerByNickname(client.getNickname());

                if(isReconnected){
                    // This starts the handle event
                    controller.addTempMVL(listener);
                    listener.addEvent(new ReconnectionRequest(client.getNickname()));
                }
                else{
                    // This starts the handle event
                    if(!controller.getMVListeners().contains(listener))
                        controller.addMVListener(listener);

                    client.setNickname(temp);
                    clients.replace(temp, client);
                    listener.nickname = temp;
                }
            }
            if(!isReconnected)  controller.addPlayerToLobby(temp, listener, oldNickname);
        }
    }

    /**
     * Method to ping RMI clients and check their connection state.
     */
    private void pong(){
        // Thread for periodic controls.
        new Thread(() -> {
            while (true) {
                synchronized (clients){
                    synchronized(toDelete){
                        for (String nickname : clients.keySet()) {
                            if(!(clients.get(nickname) instanceof ClientSkeleton)){
                                try {
                                    clients.get(nickname).ping();
                                } catch (RemoteException e) {
                                    disconnectPlayer(nickname);
                                }
                            }
                        }
                        for(String nickname : toDelete){
                            clients.remove(nickname);
                        }
                    }
                }

                try {
                    sleep(PING_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Method to disconnect a player from the server.
     * @param nickname
     */
    public void disconnectPlayer(String nickname){
        System.err.println(nickname + " is disconnected.");

        toDelete.add(nickname);

        synchronized(disconnectedClients){
            if(controller.getGame().isStarted)
                disconnectedClients.add(nickname);
        }

        ModelViewListener listener = controller.getMVListenerByNickname(nickname);

        if(listener != null) listener.stop();

        synchronized (controller.getMVListeners()){
            controller.getMVListeners().remove(listener);
            if(controller.getMVListeners().size() == 0) controller.getGame().isFinished = true;
        }
        controller.disconnectPlayer(nickname);
    }

    /**
     * Method to update the server with respect to the incoming event.
     * @param client
     * @param event
     * @throws RemoteException
     */
    @Override
    public void update(Client client, GenericEvent event) throws RemoteException{
        // If a client is connected with RMI.
        if(!(client instanceof ClientSkeleton))

            logger.addLog(event, Severity.RECEIVED);
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

    /**
     * Getter for the clients hash map.
     * @return
     */
    public HashMap<String, Client> getClients(){
        return clients;
    }

    /**
     * Method to increment the attribute 'endSent' by one.
     */
    public void notifyEndSent(){
        // If I synchronize this method, it simply doesn't work properly:
        // at the end the server and the clients don't close properly.
        synchronized (lock_end){
            endSent++;
        }
    }

    /**
     * Getter for the attribute 'endSent'.
     * @return
     */
    public int getEndSent() {
        return endSent;
    }

    /**
     * Method to stop the server.
     */
    public synchronized void restart(){

        System.exit(0);

        // TODO: se avanza tempo (lavoro già iniziato).
        // serverApp.restart();
        // return;
    }
}
