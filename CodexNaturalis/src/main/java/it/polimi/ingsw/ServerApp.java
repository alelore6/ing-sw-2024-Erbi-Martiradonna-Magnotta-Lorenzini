package it.polimi.ingsw;

import it.polimi.ingsw.Controller.Logger;
import it.polimi.ingsw.Distributed.Middleware.ClientSkeleton;
import it.polimi.ingsw.Distributed.Server;
import it.polimi.ingsw.Distributed.ServerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
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

        // Get the public address
        BufferedReader in = null;
        String ip = null;
        try {
            URL whatismyip = new URL("https://checkip.amazonaws.com");
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine();
            if(in != null) in.close();
        }
        catch(IOException e){e.printStackTrace();}

        System.setProperty("java.rmi.server.hostname", ip);
        Registry registry = LocateRegistry.createRegistry(1099);
        //Binding the server to the RMI registry so that the client can look up
        registry.rebind("CodexNaturalis_Server", server);

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
                                System.out.println(" (" + socket.getRemoteSocketAddress() + ") is connected with socket.");

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
