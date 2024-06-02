package it.polimi.ingsw;

import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;

public class ServerApp {

    private static final ServerImpl server;
    private static final Logger logger = new Logger();

    static {
        try {
            server = new ServerImpl(logger);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int SOCKET_PORT = 2002;
    private static boolean isServerExported = false;

    // TODO: gestire la disconnessione di un client con socket

    public static void main(String[] args) throws RemoteException {

        Scanner terminal = new Scanner(System.in);

        // creo server RMI
        new Thread(){
            @Override
            public void run() {
                try {
                    startRMI();
                } catch (RemoteException | AlreadyBoundException e) {
                    System.err.println("Cannot start RMI.\n");
                    e.printStackTrace();
                }

                // TODO: creare un thread per il ping ogni tot (il socket lo ha già di per sé).
                // while(true){}
            }
        }.start();

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

    //server start rmi
    private static void startRMI () throws RemoteException, AlreadyBoundException {

        Registry registry = LocateRegistry.createRegistry(45656);
        //Binding the server to the RMI registry so that the client can look up
        registry.rebind("server", server);

        System.out.println("RMI started and registered");
    }

    private static void startSocket(int port) throws RemoteException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                try{
                    Socket socket = serverSocket.accept();

                    new Thread(){
                        public void run(){
                            try{
                                ClientSkeleton clientSkeleton = new ClientSkeleton(socket, logger);
                                System.out.println(clientSkeleton.getNickname() + " (" + socket.getRemoteSocketAddress() + ") is connected with socket.");

                                while (true)    clientSkeleton.receive(server);

                            }catch(RemoteException e){}
                            finally{
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
