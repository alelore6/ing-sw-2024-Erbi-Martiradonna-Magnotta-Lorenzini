package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;


import java.rmi.RemoteException;

public class ClientImpl implements Runnable, Client{

    protected UI userInterface;
    private String nickname;
    private Server server;
    public final boolean clientFasullo;
    public boolean isRMI = false;

    public ClientImpl(Server server, boolean isTUI) throws RemoteException {
        super();

        userInterface = (isTUI ? new TUI(this) : new GUI(this));
        this.nickname = userInterface.chooseNickname();
        this.clientFasullo = false;

        initialize(server);
    }


    // This constructor is called only on the server to create a pseudo ClientImpl
    // since this is not serializable
    public ClientImpl(String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        this.clientFasullo = true;
    }

    private void initialize(Server server) throws RemoteException {

        //SOCKET
        if(server instanceof ServerStub){
            this.server = (ServerStub) server;
            this.server.register(this);
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
        //RMI
        else{
            this.server = (Server)server;
            isRMI = true;
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
        server.update(this, e);
    }


    public UI getUserInterface() {
        return userInterface;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

}

