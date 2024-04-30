package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.rmi.registry.Registry;

public class ServerApp {

    public static void main(String[] args) throws RemoteException {

        Scanner terminal = new Scanner(System.in);
        Integer port = 0;

        final int portSocket;

        port = 0;
        System.out.print("Insert Server Socket port number - 4 digit only: \n");
        while (port.toString().length() != 4){
            try {
                port = Integer.parseInt(terminal.next());
            } catch (NumberFormatException e) {
                System.out.println("It is not a valid number!!");
            }
            if (port.toString().length() != 4)
                System.out.print("Enter a 4 digit number only: ");
        }
        portSocket = port;

        // creo server RMI
        Thread rmiThread = new Thread(() -> {
            try {
                startRMI();
            } catch (RemoteException e) {
                System.err.println("Cannot start RMI.\n");
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

    }


    private static void startRMI () throws RemoteException{
        Server server = new ServerImpl();

        //Getting the registry
        Registry registry = LocateRegistry.getRegistry();
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
