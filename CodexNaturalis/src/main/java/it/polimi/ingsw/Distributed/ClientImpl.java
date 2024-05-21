package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;


import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl implements Runnable, Client, RemoteClientInterface{

    protected UI userInterface;
    private String nickname;
    private ServerStub serverStub;
    private RemoteServerInterface remoteServer;
    final boolean clientFasullo;

    public ClientImpl(Server server, boolean isTUI) throws RemoteException {
        super();

        userInterface = (isTUI ? new TUI(this) : new GUI(this));
        this.nickname = userInterface.chooseNickname();
        this.clientFasullo = false;

        initialize(server);
    }

    public ClientImpl(RemoteServerInterface server, boolean isTUI) throws RemoteException {
        super();
        userInterface = (isTUI ? new TUI(this) : new GUI(this));
        this.nickname = userInterface.chooseNickname();
        this.clientFasullo = false;
        this.remoteServer = server;
    }




    // This constructor is called only on the server to create a pseudo ClientImpl
    // since this is not serializable
    public ClientImpl(String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        this.clientFasullo = true;
    }

    private void initialize(Server server) throws RemoteException {
        if(server instanceof ServerStub){
            serverStub = (ServerStub) server;
            serverStub.register(this);
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void run() {
        userInterface.run();
    }

    @Override
    public void update(GenericEvent e) throws RemoteException {
        userInterface.update(e);
    }

    public void sendEvent(GenericEvent e) throws RemoteException {
        serverStub.update(this,e);
    }


    public UI getUserInterface() {
        return userInterface;
    }



//    @Override
//    public void sendObject(GenericEvent obj) throws RemoteException {
//
//    }
//
//    @Override
//    public GenericEvent receiveObject() throws RemoteException {
//        return null;
//    }
    @Override
    public String getNickname() {
        return nickname;
    }

    //method used by the server to SEND an event!!!
    @Override
    public void receiveObject(GenericEvent event) throws RemoteException {
        System.out.println(event.msgOutput());
        //update(event);
    }
}

