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
            server = new ServerImpl(45656);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int SOCKET_PORT = 2002;

    // ????
    //private static final int portRMI = 2003;

    // TODO: gestire la disconnessione di un client con socket

    public static void main(String[] args) throws RemoteException {

        Scanner terminal = new Scanner(System.in);

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
                startSocket(SOCKET_PORT);
            } catch (RemoteException e) {
                System.err.println("Cannot start socket.\n");
            }
        });
        socketThread.start();

        System.out.println("Socket active on port " + SOCKET_PORT + "!");
    }


    private static void startRMI () throws RemoteException, AlreadyBoundException {

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

                    System.out.println("A client (" + socket.getRemoteSocketAddress() + ") is connected with socket.");

                    new Thread(){
                        public void run(){
                            try{
                                ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                                server.register(clientSkeleton);

                                while (true)    clientSkeleton.receive(server);

                            }catch(RemoteException e){
                                System.err.println("Cannot receive client.");
                            }finally {
                                System.out.println("Closing connection.");
                                try {
                                    socket.close();
                                } catch (IOException e){
                                    System.err.println("Cannot close socket.");
                                }
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    System.err.println("Socket failed: " + e.getMessage() );
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot create server socket", e);
        }
    }
}
