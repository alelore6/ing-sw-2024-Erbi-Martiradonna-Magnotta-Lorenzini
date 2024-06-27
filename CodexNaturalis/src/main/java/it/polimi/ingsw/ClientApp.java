package it.polimi.ingsw;

import it.polimi.ingsw.Distributed.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.Distributed.Middleware.ServerStub;
import it.polimi.ingsw.Events.GenericEvent;

import static it.polimi.ingsw.Distributed.ServerImpl.PING_INTERVAL;

/**
 * Class that define the application client side.
 */
public class ClientApp {
    /**
     * The buffering character-input stream that uses a default-sized input buffer.
     * This is for taking the IP from the user.
     */
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    /**
     * The server to which the client is connected to.
     */
    private Server server;
    /**
     * The particular instance of the client.
     */
    private ClientImpl client;
    /**
     * Boolean that indicates if thread-loops are running.
     */
    private volatile boolean running = true;

    /**
     * Constructor
     * @param args the arguments taken by command line.
     * @throws InterruptedException if either Socket or RMI thread is interrupted.
     */
    private ClientApp(String[] args) throws InterruptedException {
        List<String> args_list = Arrays.asList(args);

        final int SOCKET_PORT = ServerApp.SOCKET_PORT;
        boolean isRMI   = args_list.contains("-rmi");
        boolean isTUI   = args_list.contains("-tui");
        boolean isLocal = args_list.contains("-local");
        String ip = null;

        try{
            ip = isLocal ? "localhost" : insertIP();
        }catch(IOException e){
            System.err.println("Input error. Exiting...");
            System.exit(1);
        }

        if(isRMI){   //RMI
            try{
                Registry registry = LocateRegistry.getRegistry(ip);

                server = (Server) registry.lookup("CodexNaturalis_Server");

                client = new ClientImpl(this, server, isTUI);
            } catch (RemoteException | NotBoundException e) {
                System.err.println("Connection refused. Exiting...");
                System.exit(1);
            }

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
                            if(client.getUserInterface().running) System.err.println("Can't receive from server.");
                            try {
                                UnicastRemoteObject.unexportObject(server, true);
                            } catch (NoSuchObjectException e1) {
                            }
                            running = false;
                            ClientApp.this.stop();
                        }
                    }
                }
            };

            RMIPing.start();
            RMIPing.join();
        }
        else{   //socket
            server = new ServerStub(ip, SOCKET_PORT);

            try{
                client = new ClientImpl(this, server, isTUI);
            }catch(RemoteException e){
                System.err.println("Connection refused. Exiting...");
                System.exit(1);
            }

            Thread socketThread = new Thread(){
                @Override
                public void run() {
                    while (running) {
                        GenericEvent receivedEvent = null;
                        try {
                            receivedEvent = ((ServerStub) server).receive();
                            if(receivedEvent != null)
                                client.getUserInterface().update(receivedEvent);
                        }catch(RemoteException e){
                            if(client.getUserInterface().running) System.err.println("Can't receive from server.");

                            try{
                                ((ServerStub) server).close();
                            }catch(RemoteException ex){
                                System.err.println("Fatal error. Exiting...");
                                System.exit(1);
                            }

                            running = false;
                            ClientApp.this.stop();
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

    /**
     * Method to stop the client.
     */
    public void stop(){
        running = false;
        // client.getUserInterface().stop();
        // client.getUserInterface().getListener().stop();
        System.exit(0);
    }

    /**
     * Method for entering the IP.
     * @return the IP address.
     * @throws IOException if an input error occurs
     */
    private String insertIP() throws IOException {
        System.out.println("Enter server IP address: ");

        String ip = in.readLine();

        return ip;
    }
}
