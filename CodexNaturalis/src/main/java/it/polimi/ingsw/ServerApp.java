package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.rmi.registry.Registry;

public class ServerApp {

    public static void main(String[] args) throws RemoteException {

        Scanner terminal = new Scanner(System.in);
        Integer port = -1;

        final int portSocket;

        port = -1;

        while (port < 0 || port > 65535){
            System.out.print("Enter server port number (between 0 and 65535 included): ");
            try {
                port = Integer.parseInt(terminal.next());
            } catch (NumberFormatException e) {
                System.out.println("It is not a valid number!!");
            }
        }
        portSocket = port;


        // TODO: creare un thread per il ping ogni tot

        // creo server RMI
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI();
            } catch (RemoteException | AlreadyBoundException e) {
                System.err.println("Cannot start RMI.\n");
                e.printStackTrace();
            }
        });
        rmiThread.start();

        // creo server socket
        Thread socketThread = new Thread(() -> {
            try {
                startSocket(portSocket);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread.start();


        /*try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection available.");
        }*/

        System.out.println("Active on port " + port + "!");
    }


    private static void startRMI () throws RemoteException, AlreadyBoundException {
        Server server = new ServerImpl();
        //Getting the registry
        Registry registry = LocateRegistry.createRegistry(45656);
        //Binding the server to the RMI registry so that the client can look up
        registry.rebind("server", server);
    }

    private static void startSocket(int port) throws RemoteException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                    ServerImpl server = new ServerImpl();
                    server.register(clientSkeleton);
                    while (true) {
                        clientSkeleton.receive(server);
                    }
                } catch (IOException e) {
                    System.err.println("Socket failed: " + e.getMessage() );
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot create server socket", e);
        }
    }
}
