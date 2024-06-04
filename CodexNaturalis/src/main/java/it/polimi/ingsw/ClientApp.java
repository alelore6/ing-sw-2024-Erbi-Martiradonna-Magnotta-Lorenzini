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

public class ClientApp {

    private static final int PING_INTERVAL = 100; // milliseconds

    public static void main(String[] args) throws RemoteException, NotBoundException {

        List<String> args_list = Arrays.stream(args).toList();

        final Scanner in = new Scanner(System.in);
        final int SOCKET_PORT = ServerApp.SOCKET_PORT;
        boolean isRMI = (args_list.contains("-rmi") ? true : false);
        boolean isTUI = (args_list.contains("-tui") ? true : false);
        boolean isLocal = (args_list.contains("-local") ? true : false);
        String ip;

        if(isLocal){
            ip = "localhost";
        }
        else{
            System.out.println("Enter server IP address: ");
            ip = in.next();
        }

        if(isRMI){   //RMI

            Registry registry = LocateRegistry.getRegistry(ip,1099);

            Server server = (Server) registry.lookup("server");

            ClientImpl client = new ClientImpl(server, isTUI);

            new Thread(){
                @Override
                public void run(){
                    while(true){
                        try {
                            sleep(PING_INTERVAL);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        // sendPing();
                    }
                }
            }; // TODO: inserisci start quando completato
        }
        else{   //socket

            ServerStub serverStub = new ServerStub(ip, SOCKET_PORT);
            ClientImpl client = new ClientImpl(serverStub, isTUI);
            new Thread(){
                @Override
                public void run() {
                    while (true) {
                        GenericEvent receivedEvent = null;
                        try {
                            receivedEvent = serverStub.receive(client);
                            if(receivedEvent != null)
                                client.getUserInterface().update(receivedEvent);
                        }catch(RemoteException e){
                            client.getUserInterface().printErr("Cannot receive from server.");

                            try{
                                serverStub.close();
                            }catch(RemoteException ex){
                                client.getUserInterface().printErr("Cannot close connection with server.");
                            }

                            System.exit(1);
                        }
                    }
                }
            }.start();
        }
    }
}
