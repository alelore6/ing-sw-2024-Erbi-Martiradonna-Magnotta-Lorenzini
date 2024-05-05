package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.ClientImpl;
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

        Scanner input = new Scanner(System.in);

        int networkType = -1;
        int typeView = -1;

        TUI startingUI = new TUI("fetus");

        typeView    = startingUI.chooseView();
        networkType = startingUI.chooseConnection();

        if(networkType == 1){
            //RMI
            Registry registry = LocateRegistry.getRegistry();
            Server server = (Server) registry.lookup("server");

            ClientImpl client = new ClientImpl(server, typeView, startingUI.chooseNickname());
            client.run();
        }
        else{
            //socket
            System.out.print("\nEnter server IP address: ");
            String ip = input.next();

            System.out.print("\nEnter server port number: ");
            int port = Integer.parseInt(input.next());


            ServerStub serverStub = new ServerStub(ip, port);
            ClientImpl client = new ClientImpl(serverStub, typeView, startingUI.chooseNickname());
            new Thread(){
                @Override
                public void run() {
                    while (true) {
                        try {
                            serverStub.receive(client);
                        } catch (RemoteException e) {
                            System.err.println("\nCannot receive from server.");
                            try {
                                serverStub.close();
                            } catch (RemoteException ex) {
                                System.err.println("\nCannot close connection with server.");
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
