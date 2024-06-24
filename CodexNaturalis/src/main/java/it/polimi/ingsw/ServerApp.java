package it.polimi.ingsw;

import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.rmi.registry.Registry;

import static java.lang.Thread.sleep;

/**
 * Class that define the application server side.
 */
public class ServerApp {

    /**
     * The particular instance of the client.
     */
    private static ServerImpl server;
    /**
     * The logger.
     */
    private static final Logger logger = new Logger();
    /**
     * Boolean that indicates if thread-loops are running.
     */
    private volatile boolean running = true;
    /**
     * The socket instance to accept socket connections.
     */
    private Socket socket = null;
    /**
     * The registry instance to accept RMI connections.
     */
    private Registry registry = null;
    /**
     * Attribute that represents the port where socket connections are open.
     */
    public static final int SOCKET_PORT = 2002;

    /**
     * Constructor
     *
     * @throws InterruptedException
     */
    public ServerApp() throws InterruptedException {
        try {
            server = new ServerImpl(this, logger);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        Scanner terminal = new Scanner(System.in);

        // creo server RMI
        try {
            startRMI();
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Cannot start RMI.\n");
            e.printStackTrace();
        }

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

        socketThread.join();
    }

    public static void main(String[] args) throws RemoteException {
        try{
            new ServerApp();
        }catch (InterruptedException ignored){}
    }

    /**
     * Method to start RMI connections.
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    private void startRMI () throws RemoteException, AlreadyBoundException {
        new Thread(){
            @Override
            public void run(){
                try{
                    if(registry == null){
                        registry = LocateRegistry.createRegistry(1099);
                    }

                    //Binding the server to the RMI registry so that the client can look up
                    registry.rebind("CodexNaturalis_Server", server);

                    System.out.println("RMI started and registered");
                }catch (RemoteException e){
                    System.err.println("Cannot start RMI.\n");
                }
            }
        }.start();
    }

    /**
     * Method to start socket connections.
     * @param port
     * @throws RemoteException
     */
    private void startSocket(int port) throws RemoteException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (running) {
                try{
                    socket = serverSocket.accept();

                    new Thread(){
                        @Override
                        public void run(){
                            ClientSkeleton clientSkeleton = null;
                            try{
                                clientSkeleton = new ClientSkeleton(socket, logger);
                                System.out.println(" (" + socket.getRemoteSocketAddress() + ") is connected with socket.");

                                while (running)    clientSkeleton.receive(server);

                            }catch(RemoteException e){
                                server.disconnectPlayer(clientSkeleton.getNickname());
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

    /**
     * Method to restart the server.
     */
    public void restart(){


        try{
            Naming.unbind("CodexNaturalis_Server");
            UnicastRemoteObject.unexportObject(server, true);
            registry = LocateRegistry.getRegistry();
        }catch (Exception e){
            e.printStackTrace();
        }

        running = false;

        try{
            new ServerApp();
        }catch (InterruptedException ignored){}
    }
}
