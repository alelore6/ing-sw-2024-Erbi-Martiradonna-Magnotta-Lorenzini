package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.*;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.View.TUI;

import static it.polimi.ingsw.Distributed.ServerImpl.PING_INTERVAL;
import static it.polimi.ingsw.ServerApp.SOCKET_PORT;

public class ClientApp{

    private volatile boolean running = true;
    private ClientImpl client;
    private Server server;

    private ClientApp(String[] args) throws InterruptedException {
        List<String> args_list = Arrays.asList(args);

        boolean isRMI   = args_list.contains("-rmi");
        boolean isTUI   = args_list.contains("-tui");
        boolean isLocal = args_list.contains("-local");
        boolean isConnected;
        final Scanner in = new Scanner(System.in);
        String ip;

        if(isLocal) ip = "localhost";
        else{
            System.out.println("Enter server IP address: ");
            ip = in.next();
        }

        if(isRMI){   //RMI
            do{
                isConnected = true;
                try{
                    Registry registry = LocateRegistry.getRegistry(ip);

                    Server server = (Server) registry.lookup("CodexNaturalis_Server");

                    client = new ClientImpl(server, isTUI);

                }catch (NotBoundException | RemoteException e) {
                    System.err.println("Can't connect with RMI.");
                    isConnected = false;
                }
            }while(!isConnected);

            Thread RMIPing = new Thread(){
                @Override
                public void run(){
                    while(running){
                        try {
                            sleep(PING_INTERVAL);
                            server.ping();
                        } catch (RemoteException | InterruptedException | NullPointerException e) {
                            System.err.println("Cannot receive from server.");
                            System.exit(0);
                        }
                    }
                }
            };

            RMIPing.start();
            RMIPing.join();
        }
        else{   //socket
            do{
                isConnected = true;
                try{
                    server = new ServerStub(ip, SOCKET_PORT);
                    client = new ClientImpl(server, isTUI);
                }catch (RemoteException e) {
                    System.err.println("Can't connect with Socket.");
                    isConnected = false;
                }
            }while (!isConnected);

            Thread socketThread = new Thread(() -> {
                while (running) {
                    GenericEvent receivedEvent = null;
                    try {
                        receivedEvent = ((ServerStub) server).receive(client);
                        if(receivedEvent != null)
                            client.getUserInterface().update(receivedEvent);
                    }catch(RemoteException e){
                        System.err.println("Cannot receive from server.");

                        try{
                            ((ServerStub) server).close();
                        }catch(RemoteException ex){
                            System.err.println("Fatal error.");
                            System.exit(1);
                        }

                        System.exit(0);
                    }
                }
            });

            socketThread.start();
            socketThread.join();
        }
    }

    public static void main(String[] args){
        try{
            new ClientApp(args);
        }catch(InterruptedException e){
            System.out.println("The program was interrupted.");
        }
    }
}
