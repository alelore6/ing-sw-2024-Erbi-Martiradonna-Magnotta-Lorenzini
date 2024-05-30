package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.ClientRegister;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Runnable, Client{

    protected UI userInterface;
    private String nickname;
    private Server server;
    public final boolean clientFasullo;
    public final boolean isRMI;

    public ClientImpl(Server server, boolean isTUI) throws RemoteException {
        super();

        isRMI = server instanceof ServerStub ? false : true;
        clientFasullo = false;
        userInterface = isTUI ? new TUI(this) : new GUI(this);
        run();
        nickname = userInterface.chooseNickname();

        initialize(server);
    }

    private void initialize(Server server) throws RemoteException {
        // Socket
        if(server instanceof ServerStub){
            this.server = server;
            this.server.register(this);
            userInterface.notifyListener(new ClientRegister(this));
            userInterface.getListener().handleEvent();
        }
        // RMI
        else{
            this.server = server;
            // The RMI registration is already implicitly happened
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

