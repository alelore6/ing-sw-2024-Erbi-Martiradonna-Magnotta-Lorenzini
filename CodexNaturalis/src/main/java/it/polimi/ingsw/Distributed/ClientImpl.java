package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;
import it.polimi.ingsw.View.View;


import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl  extends UnicastRemoteObject implements Runnable, Client{

    private UI view;
    private String nickname;
    private int viewType; //1 if GUI, 0 if CLI
    private int networkType; // 0 rmi, 1 socket

    public ClientImpl(Server server, String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        initialize(server);

        if(viewType == 1){
            // TODO GUI:
             view = new GUI(nickname);
        }
        else if(viewType == 0){
            view = new TUI(nickname);
        }
    }
    //other constructors needed for overloading
    public ClientImpl(int port, Server server, String nickname) throws RemoteException {
        super(port);

        this.nickname=nickname;
        initialize(server);

        if(viewType == 1){
            view = new GUI(nickname);
        }
        else if(viewType == 0){
            view = new TUI(nickname);
        }
    }

    public ClientImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf, Server server, String nickname) throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
        this.nickname=nickname;
        if(viewType == 1){
            view = new GUI(nickname);
        }
        else if(viewType == 0){
            view = new TUI(nickname);
        }
    }

    private void initialize(Server server) throws RemoteException {
        server.register(this);
    }

    public String getNickname() {
        return nickname;
    }

    protected void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void run() {
        view.run();
    }

    @Override
    public void update(Client client, GenericEvent e) throws RemoteException {
        view.update(e);
    }

    public UI getView() {
        return view;
    }
}
