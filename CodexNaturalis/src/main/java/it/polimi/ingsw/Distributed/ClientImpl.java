package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Listeners.ModelListener;
import it.polimi.ingsw.Listeners.ViewListener;
import it.polimi.ingsw.Messages.Events;
import it.polimi.ingsw.View.TextualUI;


import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl  extends UnicastRemoteObject implements Runnable, Client{

    TextualUI view ;
    //represent the player
    public String nickname;
    private ViewListener vcListener;
    private ModelListener mvListener;


    public ClientImpl(Server server,int viewType) throws RemoteException {
        // this.vcListener=new ViewListener(server.Controller,this);
        super();
        //if viewType
        view= new TextualUI();
        initialize(server);
    }
    //other constructors needed for overloading
    public ClientImpl(int port, Server server, int viewType) throws RemoteException {
        super(port);
        //if viewType
        view= new TextualUI();
        initialize(server);
    }

    public ClientImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf, Server server) throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
    }

    private void initialize(Server server) throws RemoteException {
        server.register(this);
        view.addObserver((v, e)-> {
            try{
                server.update(this, e);
            }catch(RemoteException e1){
                System.err.println("Error while updating: "+ e1.getMessage() + ". Skipping update..");
            }
        });
    }


    @Override
    public void run() throws RemoteException {
        view.run();
    }

    @Override
    public void update(PreviousView v, Events e) throws RemoteException {
        this.view.update(v, e);
    }
}
