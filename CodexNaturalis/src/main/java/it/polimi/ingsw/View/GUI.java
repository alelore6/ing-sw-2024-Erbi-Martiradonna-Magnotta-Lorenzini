package it.polimi.ingsw.View;

import it.polimi.ingsw.Distributed.ClientImpl;
import it.polimi.ingsw.Events.GenericEvent;
import it.polimi.ingsw.Listeners.ViewControllerListener;
import it.polimi.ingsw.model.Card;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import static java.lang.String.valueOf;

public class GUI extends UI{

    private static CustomFrame f;
    public GUI(ClientImpl client) {
        super(client);
    }

    @Override
    protected void printCard(Card card) {

    }

    protected void printCard(int id, boolean isFacedown, int x, int y, double scale) {
        String idString;
        if(id < 100){
            idString = valueOf(id);
            idString = "0" + idString;

            if(id<10){
                idString = "0" + idString;
            }
        }
        else{
            idString = valueOf(id);
        }

        f.setPrintPath("assets/images/cards/" + (isFacedown ? "back" : "front") + "/" + idString + ".png");
        f.setCoord(x, y, scale);
    }

    @Override
    public void update(GenericEvent e){

    }

    @Override
    public void notifyListener(ViewControllerListener listener, GenericEvent e) {

    }

    @Override
    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                new Thread(){
                    @Override
                    public void run() {
                        f = new CustomFrame("CodexNaturalis");
                        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        f.setSize(1280,720);
                        f.setVisible(true);
                        printCard(102,false,100,100,0.6);

//                        while(true){
//                            //gestione eventi continuo come nella TUI
//                        }
                    }
                }.start();

            }
        });
    }
}
