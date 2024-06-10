package it.polimi.ingsw.Graphical;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.ModelView.PlayerView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class PersonalPanel extends JSplitPane {
    protected final String nickname;
    private final PlayerView playerView;
    private int choice;
    private ArrayList<JButton> playButtons;
    private  ArrayList<JLabel> labels;
    private int[] cardsID= new int[3];
    private boolean[] isFacedown= new boolean[3];
    private JLabel selectedLabel;

    PersonalPanel(PlayerView playerView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.nickname = playerView.nickname;
        this.playerView = playerView;
        playButtons = new ArrayList<>();
        labels = new ArrayList<>();

        JPanel leftPanel = new JPanel(); // questo sarebbe playedcards panel
        leftPanel.setBackground(Color.decode("#d9dbc1"));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i <= 4; i++) {
            JPanel imagePanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);

            JLabel label = new JLabel();
            int k = ((i - 1) * 25) + 1;
            label.setIcon(getImageIcon(GUI.getCardPath(k, false))); //mettere la carta al posto di k

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 2;
            imagePanel.add(label, gbc);

            if (i != 1) {
                //carte della mano
                cardsID[i-2]=k;
                isFacedown[i-2]=false;
                labels.add(i-2,label);
                // Creazione e aggiunta del bottone Flip
                JButton flipButton = new JButton("Flip");
                final int index = i-2;

                flipButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //devo anche girare la carta veramente
                        isFacedown[index] = !isFacedown[index];
                        label.setIcon(getImageIcon(GUI.getCardPath(cardsID[index], isFacedown[index])));
                    }
                });
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 1;
                imagePanel.add(flipButton, gbc);

                // Creazione e aggiunta del bottone Play
                JButton playButton = new JButton("Play");
                playButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        choice = index;
                        System.out.println("You have chosen card N. "+ (choice+1)+ " ID: "+cardsID[choice]);
                        if (selectedLabel != null) {
                            selectedLabel.setBorder(null);
                        }
                        selectedLabel = label;
                        selectedLabel.setBorder(new LineBorder(Color.GREEN, 4));
                        confirmPlay();
                    }
                });
                playButtons.add(playButton);
                gbc.gridy = 1;
                imagePanel.add(playButton, gbc);
            }

            rightPanel.add(imagePanel);
            rightPanel.add(Box.createVerticalStrut(5)); // Riduce lo spazio verticale tra le carte
        }

        hidePlayButton();
        add(leftPanel, JSplitPane.LEFT);
        add(rightPanel, JSplitPane.RIGHT);

        setDividerLocation(1140); // Imposta la posizione iniziale del divisore

        // Impostazione della larghezza minima e massima dei pannelli
        leftPanel.setMinimumSize(new Dimension(900, 300));
        rightPanel.setMinimumSize(new Dimension(390, 300));
    }

    public static void main(String[] args) {
        // Creazione del frame principale
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen

        PersonalPanel panel = new PersonalPanel(new PlayerView(new Player("test", null)));

        // Aggiunta di JSplitPane al frame
        frame.add(panel);

        // Visualizzazione del frame
        frame.setVisible(true);

        panel.update(null,true);
    }

    private void showPlayButton() {
        for(JButton b : playButtons){
            b.setVisible(true);
        }
    }

    private void hidePlayButton() {
        for(JButton b : playButtons){
            b.setVisible(false);
        }
    }

    private ImageIcon getImageIcon(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));
    }

    public int getChoice() {
        return choice;
    }

    private void confirmPlay(){
        if(selectedLabel!=null){
            int i=JOptionPane.showConfirmDialog(this,"Confirm the play");
            if(i==0) {//la giocata viene confermata
                hidePlayButton();
            }
        }
    }


    protected void update(PlayerView playerView, boolean play ) {
        if (play) showPlayButton();
    }
}
