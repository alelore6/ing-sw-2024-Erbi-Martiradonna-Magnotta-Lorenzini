package it.polimi.ingsw;

import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
    private ServerApp() throws InterruptedException {
        try {
            server = new ServerImpl(this, logger);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // create RMI server
        try {
            startRMI();
        } catch (RemoteException | AlreadyBoundException e) {
            System.err.println("Cannot start RMI.\n");
            e.printStackTrace();
        }

        // create socket server
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

    public static void main(String[] args){
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
        new Thread(() -> {
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
        }).start();
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

                    new Thread(() -> {
                        ClientSkeleton clientSkeleton = null;
                        try{
                            clientSkeleton = new ClientSkeleton(socket, logger);
                            System.out.println(" (" + socket.getRemoteSocketAddress() + ") is connected with socket.");

                            while (running)    clientSkeleton.receive(server);

                        }catch(RemoteException e){
                            assert clientSkeleton != null;
                            server.disconnectPlayer(clientSkeleton.getNickname());
                        }
                    }).start();
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
    public void stop(){
        // System.exit(0);
    }
}
