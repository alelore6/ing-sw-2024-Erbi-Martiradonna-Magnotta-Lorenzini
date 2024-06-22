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

public class ServerApp {

    private static ServerImpl server;
    private static final Logger logger = new Logger();
    private volatile boolean running = true;
    private Socket socket = null;
    private Registry registry = null;

    public static final int SOCKET_PORT = 2002;

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

    //server start rmi
    private void startRMI () throws RemoteException, AlreadyBoundException {

        if(registry == null){
//            // Get the public address
//            BufferedReader in = null;
//            String ip = null;
//            try {
//                URL whatismyip = new URL("https://checkip.amazonaws.com");
//                in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
//                ip = in.readLine();
//                if(in != null) in.close();
//            }
//            catch(IOException e){e.printStackTrace();}
//
//            System.setProperty("java.rmi.server.hostname", ip);
            registry = LocateRegistry.createRegistry(1099);
        }

        //Binding the server to the RMI registry so that the client can look up
        registry.rebind("CodexNaturalis_Server", server);

        System.out.println("RMI started and registered");
    }

    private void startSocket(int port) throws RemoteException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (running) {
                try{
                    socket = serverSocket.accept();

                    new Thread(){
                        public void run(){
                            try{
                                ClientSkeleton clientSkeleton = new ClientSkeleton(socket, logger);
                                System.out.println(" (" + socket.getRemoteSocketAddress() + ") is connected with socket.");

                                while (running)    clientSkeleton.receive(server);

                            }catch(RemoteException ignored){}
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

    public void restart(){


        try{
            Naming.unbind("rmi://localhost/HelloServer");
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
