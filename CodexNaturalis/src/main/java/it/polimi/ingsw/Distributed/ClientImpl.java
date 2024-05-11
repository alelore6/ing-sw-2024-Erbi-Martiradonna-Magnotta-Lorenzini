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

public class ClientImpl extends UnicastRemoteObject implements Runnable, Client, Serializable{

    private UI userInterface;
    private String nickname;
    private int viewType; //1 if TUI, 2 if GUI
    private int networkType; // 1 RMI, 2 PrivateSocket
    private ServerStub serverStub;

    public ClientImpl(Server server, int viewType, String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        this.viewType = viewType;

        // The else statement is equivalent to the condition that typeView == 2
        // because typeView is either 1 or 2 after the chooseView() call.
        // In this way there is no warning about userInterface initialization.
        if(viewType == 1){
            userInterface = new TUI(this);
        }
        else{
            // TODO GUI:
            userInterface = new GUI(this);
        }
        initialize(server);
    }
    //other constructors needed for overloading
    public ClientImpl(int port, Server server) throws RemoteException {
        super(port);
        initialize(server);
    }

    public ClientImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf, Server server) throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
    }

    public ClientImpl(String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
    }

    private void initialize(Server server) throws RemoteException {
        if(server instanceof ServerStub){
            serverStub = (ServerStub) server;
            serverStub.register(this);
            userInterface.notifyListener(userInterface.getListener(), new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
    }

    public String getNickname() {
        return nickname;
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
}
