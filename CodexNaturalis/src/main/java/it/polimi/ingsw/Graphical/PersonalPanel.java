package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.ModelView.PlayerView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PersonalPanel extends JSplitPane {
    protected final String nickname;

     PersonalPanel(PlayerView playerView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.nickname = playerView.nickname;
    }



        public static void main(String[] args) {
            // Creazione del frame principale
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen

            PersonalPanel panel=new PersonalPanel(new PlayerView(new Player("test",null)));

            JPanel leftPanel = new JPanel();
            //leftPanel.setBackground(Color.BLUE);

            JPanel rightPanel = new JPanel();
            //rightPanel.setBackground(Color.GREEN);
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

            for (int i = 1; i <= 4; i++) {
                JPanel imagePanel = new JPanel();
                imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                //imagePanel.setBackground(Color.GREEN);

                // Creazione e aggiunta dell'immagine
                JLabel label = new JLabel();
                int k= ((i-1)*25)+1;
                label.setIcon(panel.getImageIcon(GUI.getCardPath(k,false), 0,0));
                imagePanel.add(label);

                if(i!=1){
                    // Creazione e aggiunta del bottone
                    JButton button = new JButton("Flip");
                    final int index = i;// Variabile finale per usarla all'interno del listener
                    final boolean[] isFacedown = new boolean[3];
                    for(int j=0;j<3;j++){
                        isFacedown[j]=false;
                    }
                    final int cardID = k;
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //Dovrei anche ruotare la carta veramente
                            isFacedown[index-2]=!isFacedown[index-2];
                            label.setIcon(panel.getImageIcon(GUI.getCardPath(k,isFacedown[index-2]),0,0));
                        }
                    });
                    imagePanel.add(button);
                }

                // Aggiunta del pannello contenente immagine e bottone al pannello di destra
                rightPanel.add(imagePanel);


                rightPanel.add(Box.createVerticalStrut(10));

            }


            panel.add(leftPanel,JSplitPane.LEFT);
            panel.add(rightPanel,JSplitPane.RIGHT);

            panel.setDividerLocation(1140); // Imposta la posizione iniziale del divisore

            // Impostazione della larghezza minima e massima dei pannelli
            leftPanel.setMinimumSize(new Dimension(800, 300));
            rightPanel.setMinimumSize(new Dimension(390, 300));

            // Aggiunta di JSplitPane al frame
            frame.add(panel);

            // Visualizzazione del frame
            frame.setVisible(true);
        }


    private ImageIcon getImageIcon(String path, int scaleX, int scaleY) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));
    }

    protected void update(PlayerView playerView) {

    }
}
