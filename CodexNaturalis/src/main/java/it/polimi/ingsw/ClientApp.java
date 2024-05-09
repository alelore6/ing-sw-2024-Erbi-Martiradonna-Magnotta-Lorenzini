package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.PrivateSocket;
import it.polimi.ingsw.Distributed.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.View.GUI;
import it.polimi.ingsw.View.TUI;
import it.polimi.ingsw.View.UI;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientApp {
    public static void main( String[] args ) throws RemoteException, NotBoundException {

        int networkType = -1;
        int typeView = -1;

        TUI startingTUI = new TUI("fetus");

        typeView    = startingTUI.chooseView();
        networkType = startingTUI.chooseConnection();

        if(networkType == 1){   //RMI

            Registry registry = LocateRegistry.getRegistry();
            Server server = (Server) registry.lookup("server");

            ClientImpl client = new ClientImpl(server, typeView, startingTUI.chooseString("nickname"));
            client.run();
        }
        else{   //socket

            PrivateSocket socket = startingTUI.setupSocket();

            ServerStub serverStub = new ServerStub(socket);
            ClientImpl client = new ClientImpl(serverStub, typeView, startingTUI.chooseString("nickname"));
            new Thread(){
                @Override
                public void run() {
                    while (true) {
                        try {
                            serverStub.receive(client);
                        } catch (RemoteException e) {
                            startingTUI.printErr("Cannot receive from server.");

                            try {
                                serverStub.close();
                            } catch (RemoteException ex) {
                                startingTUI.printErr("Cannot close connection with server.");
                            }

                            System.exit(1);
                        }
                    }
                }
            }.start();
            client.run();
        }
    }
}
