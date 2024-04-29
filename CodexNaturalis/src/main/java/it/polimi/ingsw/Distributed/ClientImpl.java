package it.polimi.ingsw.Distributed;


import it.polimi.ingsw.Events.Generic;
import it.polimi.ingsw.Events.JoinServer;
import it.polimi.ingsw.View.TextualUI;
import it.polimi.ingsw.View.View;


import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl  extends UnicastRemoteObject implements Runnable, Client{

    View view;
    public String nickname;
    private int viewType; //1 if GUI, 0 if CLI
    private int networkType; // 0 rmi, 1 socket

    public ClientImpl(Server server, String nickname) throws RemoteException {
        // this.vcListener=new ViewListener(server.controller,this);
        super();
        view=new TextualUI();
        this.nickname=nickname;
        initialize(server);
    }
    //other constructors needed for overloading
    public ClientImpl(int port, Server server, String nickname) throws RemoteException {
        super(port);
        view=new TextualUI();
        this.nickname=nickname;
        initialize(server);
    }

    public ClientImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf, Server server, String nickname) throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
        this.nickname=nickname;
    }

    private void initialize(Server server) throws RemoteException {
        server.register(this);
        // creare il listener
        JoinServer ev = new JoinServer();
        view.addListener((v, e)-> { // TODO: fixare
            try{
                server.update(this, ev);
            }catch(RemoteException e1){
                System.err.println("Error while updating: "+ e1.getMessage() + ". Skipping update..");
            }
        });
    }

    @Override
    public void run() {
        view.run();
    }

    @Override
    public void update(View v, Generic e) throws RemoteException {
        view.update(v, e);
    }
}
