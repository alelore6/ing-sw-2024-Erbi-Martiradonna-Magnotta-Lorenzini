package it.polimi.ingsw.Distributed;

import it.polimi.ingsw.Listener.ModelListener;
import it.polimi.ingsw.Listener.ViewListener;


import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl  extends UnicastRemoteObject implements Runnable, Client{

    TextualUI view = new TextualUI();

    private int viewType; //1 if GUI, 0 if CLI
    private int networkType; // 0 rmi, 1 socket
    private ViewListener vcListener;
    private ModelListener mvListener;


    public ClientImpl(Server server) throws RemoteException {
        // this.vcListener=new ViewListener(server.controller,this);
        super();

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
