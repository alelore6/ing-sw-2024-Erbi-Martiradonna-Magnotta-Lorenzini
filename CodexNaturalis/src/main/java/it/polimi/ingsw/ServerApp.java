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

    private static final ServerImpl server;

    static {
        try {
            server = new ServerImpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private static int portSocket = 0;
    // TODO: gestire la disconnessione di un client con socket
    private static boolean[] ports = new boolean[4];

    public static void main(String[] args) throws RemoteException {

        Scanner terminal = new Scanner(System.in);
        Integer port = -1;
        for(boolean p : ports)   p = false;

        port = -1;

        while (port < 0 || port > 65532){
            System.out.print("Enter server port number (between 0 and 65532 included): ");
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
        Thread socketThread1 = new Thread(() -> {
            try {
                startSocket(portSocket + 0);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread1.start();

        Thread socketThread2 = new Thread(() -> {
            try {
                startSocket(portSocket + 1);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread2.start();

        Thread socketThread3 = new Thread(() -> {
            try {
                startSocket(portSocket + 2);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread3.start();

        Thread socketThread4 = new Thread(() -> {
            try {
                startSocket(portSocket + 3);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread4.start();

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
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                try{
                    Socket socket = serverSocket.accept();
                    ports[port - portSocket] = true;

                    ClientSkeleton clientSkeleton = new ClientSkeleton(socket);

                    server.register(clientSkeleton);

                    System.out.println("A client is connected on port " + port + ".");

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
