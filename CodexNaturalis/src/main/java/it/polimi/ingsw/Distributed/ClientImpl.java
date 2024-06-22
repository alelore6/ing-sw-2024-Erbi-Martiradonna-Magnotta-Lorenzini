package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.ClientApp;
import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client{

    public final ClientApp clientApp;
    protected UI userInterface;
    private String nickname;
    public final Server server;
    public final boolean isRMI;

    public ClientImpl(ClientApp clientApp, Server server, boolean isTUI) throws RemoteException {
        super();

        this.clientApp = clientApp;
        isRMI = server instanceof ServerStub ? false : true;
        userInterface = isTUI ? new TUI(this) : new GUI(this);
        run();
        nickname = userInterface.chooseNickname();

        this.server = server;
        initialize();
    }

    private void initialize() throws RemoteException {
        // Socket
        if(server instanceof ServerStub){
            ((ServerStub) server).register(this);
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
        // RMI
        else{
            // The RMI registration is already implicitly happened
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
    }

    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void run() {
        userInterface.run();
    }

    @Override
    public void ping() throws RemoteException{}

    @Override
    public void update(GenericEvent e){
        userInterface.update(e);
    }

    public void sendEvent(GenericEvent e) throws RemoteException {
        server.update(this, e);
    }

    public UI getUserInterface() {
        return userInterface;
    }

    @Override
    public String getNickname(){
        return nickname;
    }
}

