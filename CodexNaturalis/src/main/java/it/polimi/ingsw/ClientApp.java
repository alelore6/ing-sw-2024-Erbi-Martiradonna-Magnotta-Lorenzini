package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Distributed.Middleware.ServerStub;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientApp {
    public static void main( String[] args ) throws RemoteException, NotBoundException {

        Scanner input = new Scanner(System.in);
        int networkType = -1;
        int typeView = -1;

        System.out.print("Choose if you want a RMI or Socket client: 0 for RMI and 1 for Socket: \n");
        while(networkType != 0 || networkType != 1){
            try{
                networkType = Integer.parseInt(input.next());
            } catch (NumberFormatException e) {
                System.out.println("It is not a valid number!!\n");
            }
            if(networkType != 0 && networkType != 1)
                System.out.print("Enter 0 for RMI or 1 for Socket: \n");

        }

        System.out.print("Choose if you wanna play from CLI or GUI: 0 for CLI and 1 for GUI: \n");
        while(typeView != 0 || typeView != 1){
            try{
                typeView = Integer.parseInt(input.next());
            } catch (NumberFormatException e) {
                System.out.println("It is not a valid number!!\n");
            }
            if(typeView != 0 && typeView != 1)
                System.out.print("Enter 0 for CLI or 1 for GUI: \n");
        }

        if(networkType ==0) {
            //RMI
            Registry registry = LocateRegistry.getRegistry();
            Server server = (Server) registry.lookup("Server");

            ClientImpl client = new ClientImpl(server,typeView);
            client.run();
        }
        else{
            //socket
            System.out.print("Enter server IP address: ");
            String ip = input.next();
            System.out.println("Server IP address is: " + ip);

            System.out.print("Enter server port number: ");
            int port = Integer.parseInt(input.next());
            System.out.println("Server port number is: " + port);

            ServerStub serverStub = new ServerStub(ip, port);
            ClientImpl client = new ClientImpl(serverStub,typeView);
            new Thread(){
                @Override
                public void run() {
                    while (true) {
                        try {
                            serverStub.receive(client);
                        } catch (RemoteException e) {
                            System.err.println("Cannot receive from server.\n");
                            try {
                                serverStub.close();
                            } catch (RemoteException ex) {
                                System.err.println("Cannot close connection with server.\n");
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
