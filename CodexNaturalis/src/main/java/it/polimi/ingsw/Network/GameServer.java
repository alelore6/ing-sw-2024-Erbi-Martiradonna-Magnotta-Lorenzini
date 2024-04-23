package it.polimi.ingsw.Network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer implements Server{
    private Controller controller;
    private String[] playingUsernames;


    public static void main( String[] args ){

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(/*port number*/);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //serve una classe che rappresenti i client
        //sia tramite socket che rmi?

    }
}
