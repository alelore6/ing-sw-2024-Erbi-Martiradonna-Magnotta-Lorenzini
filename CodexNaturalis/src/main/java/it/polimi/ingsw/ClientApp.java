package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;
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

public class ClientApp {

    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private Server server;
    private ClientImpl client;

    private volatile boolean running = true;

    private ClientApp(String[] args) throws InterruptedException {
        List<String> args_list = Arrays.asList(args);

        final int SOCKET_PORT = ServerApp.SOCKET_PORT;
        boolean isRMI   = args_list.contains("-rmi");
        boolean isTUI   = args_list.contains("-tui");
        boolean isLocal = args_list.contains("-local");
        boolean isConnected;
        String ip;

        if(isRMI){   //RMI

            do{
                isConnected = true;

                try{
                    ip = isLocal ? "localhost" : insertIP();

                }catch(IOException e){
                    continue;
                }

                try{
                    Registry registry = LocateRegistry.getRegistry(ip);

                    server = (Server) registry.lookup("CodexNaturalis_Server");

                    client = new ClientImpl(server, isTUI);
                } catch (RemoteException | NotBoundException e) {
                    System.err.println("Failed to connect to server. Please, try again.");
                    isConnected = false;
                }
            }while(!isConnected);

            Thread RMIPing = new Thread(){
                @Override
                public void run(){
                    while(running){
                        try {
                            sleep(PING_INTERVAL);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            server.ping();
                        } catch (RemoteException e) {
                            System.err.println("Can't receive from server.");
                            try {
                                UnicastRemoteObject.unexportObject(server, true);
                            } catch (NoSuchObjectException ignored) {}
                            running = false;
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
                    ip = isLocal ? "localhost" : insertIP();
                }catch(IOException e){
                    continue;
                }

                server = new ServerStub(ip, SOCKET_PORT);

                try{
                    client = new ClientImpl(server, isTUI);
                }catch(RemoteException e){
                    System.err.println("Failed to connect to server. Please, try again.");
                    isConnected = false;
                }
            }while(!isConnected);

            Thread socketThread = new Thread(){
                @Override
                public void run() {
                    while (running) {
                        GenericEvent receivedEvent = null;
                        try {
                            receivedEvent = ((ServerStub) server).receive(client);
                            if(receivedEvent != null)
                                client.getUserInterface().update(receivedEvent);
                        }catch(RemoteException e){
                            client.getUserInterface().printErr("Can't receive from server.");

                            try{
                                ((ServerStub) server).close();
                            }catch(RemoteException ex){
                                client.getUserInterface().printErr("Fatal error. Exiting...");
                                System.exit(1);
                            }

                            running = false;
                            try {
                                client.getUserInterface().stop();
                                client.getUserInterface().getListener().stop();

                            } catch (InterruptedException ignored) {}
                        }
                    }
                }
            };

            socketThread.start();
            socketThread.join();
        }
    }

    public static void main(String[] args){
        ClientApp clientInstance;
        try{
            clientInstance = new ClientApp(args);
        }catch(InterruptedException ignored){}

        System.exit(0);
    }

    private String insertIP() throws IOException {
        System.out.println("Enter server IP address: ");

        String ip = in.readLine();

        return ip;
    }
}
