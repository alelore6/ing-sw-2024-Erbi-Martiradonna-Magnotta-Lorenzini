package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.ModelView.PlayerView;

import javax.swing.*;
import java.awt.*;

public class PersonalPanel extends JScrollPane {

    PersonalPanel(PlayerView playerView, JPanel panel){
        super(panel);
        panel.setBackground(Color.ORANGE);

    }

    public static void main(String[] args) {
        // Creazione del frame principale
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen

                // Creazione del JPanel con layout manager
                JPanel panel = new JPanel();
                PersonalPanel scrollPane = new PersonalPanel(null,panel);
                // Aggiunta di molti componenti al pannello per creare la necessità di scorrimento
                for (int i = 1; i <= 50; i++) {
                    panel.add(new JLabel("Label " + i));
                }

                // Creazione di JScrollPane e aggiunta del JPanel ad esso

                // Aggiunta di JScrollPane al frame
                frame.add(scrollPane);

                // Visualizzazione del frame
                frame.setVisible(true);
            }



    protected void update(PlayerView playerView) {

    }
}
