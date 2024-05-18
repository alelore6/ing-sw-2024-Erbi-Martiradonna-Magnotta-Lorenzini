package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;


import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Runnable, Client{

    protected UI userInterface;
    private String nickname;
    private ServerStub serverStub;
    final boolean clientFasullo;

    public ClientImpl(Server server, boolean isTUI) throws RemoteException {
        super();

        userInterface = (isTUI ? new TUI(this) : new GUI(this));
        this.nickname = userInterface.setNickname();
        this.clientFasullo = false;

        initialize(server);
    }
    //other constructors needed for overloading
    public ClientImpl(int port, Server server) throws RemoteException {
        super(port);
        initialize(server);
        this.clientFasullo = false;
    }

    public ClientImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf, Server server) throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
        this.clientFasullo = false;
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

    protected void setNickname(String nickname) {
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
}

