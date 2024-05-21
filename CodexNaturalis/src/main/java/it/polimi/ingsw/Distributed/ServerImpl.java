package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Events.TestEvent;
import it.polimi.ingsw.Listeners.ModelViewListener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server, RemoteServerInterface{

    public Controller controller = new Controller(this);
    private static final List<ClientImpl> CLIENT_IMPL_LIST = new ArrayList<>();
    private static int numClient = 0;
    private ArrayList<ClientSkeleton> clientSkeletons = new ArrayList<ClientSkeleton>();
    int clientSkeletonIndex = 0; //keeps track of clientskeletons without nickname
    private HashMap<RemoteClientInterface, String> RMIclients = new HashMap<>();

    //server constructor with the default rmi port
    public ServerImpl() throws RemoteException {
        super();
    }

    //server implementation with a certain RMI port
    public ServerImpl(int port) throws RemoteException {
        super(port);
    }

    @Override
    public void register(Client client) throws RemoteException{
        if(client instanceof ClientImpl){ // if MALEDETTO
            String newNickname = null;
            try{
                // check nickname
                findClientImpl(((ClientImpl) client).getNickname());
            }catch (RuntimeException e){
                // An identical nickname has been found

                ClientSkeleton temp = findLastCSbyNickname(client.getNickname());

                //add a sequential number at the end of the nickname
                ((ClientImpl) client).setNickname(((ClientImpl) client).getNickname() + numClient);

                // set the new name in ClientImpl
                newNickname = ((ClientImpl) client).getNickname();

                // set the new name in ClientSkeleton if present
                if(temp != null){
                    temp.setNickname(newNickname);
                }
            }
            CLIENT_IMPL_LIST.add((ClientImpl) client);
            numClient++;
            if (!((ClientImpl) client).clientFasullo) {
                controller.getMVListeners().add(new ModelViewListener(this, client));
            }
            controller.addPlayerToLobby(((ClientImpl) client).getNickname(), controller.getMVListenerByNickname(((ClientImpl) client).getNickname()), newNickname);

        }
        else{
            clientSkeletons.add((ClientSkeleton) client);
        }
    }

    public void findClientImpl(String nickname){
        for(ClientImpl c : CLIENT_IMPL_LIST){
            if (c.getNickname().equalsIgnoreCase(nickname))
                // client found
                throw new RuntimeException("client " + nickname + " found");
        }
    }

    public ClientSkeleton findLastCSbyNickname(String nickname){
        for(int i = clientSkeletons.size() - 1; i >= 0; i--){
            if(clientSkeletons.get(i).getNickname().equals(nickname))    return clientSkeletons.get(i);
        }

        return null;
    }

    @Override
    public void update(Client client, GenericEvent event) throws RemoteException{

        if(event instanceof ClientRegister){
            client.setNickname(event.nickname);
            controller.addMVListener(new ModelViewListener(this, client));
            controller.updateModel(event, event.nickname);
        }else {
            //client has responded to a request to modify the model
            for (int i = 0; i < CLIENT_IMPL_LIST.size(); i++) {
                if (CLIENT_IMPL_LIST.get(i).getNickname().equalsIgnoreCase(client.getNickname())) {
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


    public void sendEventToAll(GenericEvent event) throws RemoteException {
        for(ClientSkeleton client : clientSkeletons)    sendEvent(client, event);

    }

    public void sendEvent(Client client, GenericEvent event) throws RemoteException {
        client.update(event);
    }

    //method used by the client to SEND an event. then the event and the client are passed to the general update() method.
    @Override
    public void processEvent(GenericEvent event, Client client) throws RemoteException {
        System.out.println("received"+ event.toString());
        update(client, event);
    }

    //method used only 1 time to register the client stub to the server and the associated nickname
    @Override
    public void processClient(RemoteClientInterface remoteClient, String nickname) throws RemoteException {
        RMIclients.put(remoteClient, nickname);
        //RIGHE DI TEST
        remoteClient.receiveObject(new TestEvent("SONO ARRIVATO AL CLIENT TRAMITE RMI", nickname));
    }
}
