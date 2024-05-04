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

        @Deprecated
        Scanner input = new Scanner(System.in);

        int networkType = -1;
        int typeView = -1;

        UI userInterface = new TUI("fetus");

        System.out.print("\nChoose if you wanna play from CLI or GUI: 1 for CLI and 2 for GUI:");

        userInterface.choose(2);

        // set nickname

                if(typeView == 1)   userInterface = new TUI(nickname);
        else    if(typeView == 2)   userInterface = new GUI(nickname);

        System.out.print("\nChoose if you want a RMI or Socket client: 0 for RMI and 1 for Socket:");
        while(networkType != 0 && networkType != 1){
            try{
                networkType = Integer.parseInt(input.next());
            } catch (NumberFormatException e) {
                System.out.println("It is not a valid number!!\n");
            }
            if(networkType != 0 && networkType != 1)
                System.out.print("Enter 0 for RMI or 1 for Socket: \n");
        }

        if(networkType ==0) {
            //RMI
            Registry registry = LocateRegistry.getRegistry();
            Server server = (Server) registry.lookup("server");

            ClientImpl client = new ClientImpl(typeView, server, nickname);
            client.run();
        }
        else{
            //socket
            System.out.print("\nEnter server IP address: ");
            String ip = input.next();

            System.out.print("\nEnter server port number: ");
            int port = Integer.parseInt(input.next());


            ServerStub serverStub = new ServerStub(ip, port);
            ClientImpl client = new ClientImpl(typeView, serverStub, nickname);
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
