package it.polimi.ingsw.Graphical;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.StartingCard;
import it.polimi.ingsw.ModelView.PlayerView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
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
    private final PlayerPanel playerPanel;
    private int choice;
    private ArrayList<JButton> playButtons;
    private  ArrayList<JLabel> labels;
    private int[] cardsID= new int[3];
    private boolean[] isFacedown= new boolean[3];
    private JLabel selectedLabel;
    private boolean playing = false;
    private ImageIcon possiblePlayImage =null;
    private final Object lock;


    PersonalPanel(PlayerView playerView, Object lock) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.nickname = playerView.nickname;
        this.playerView = playerView;
        this.lock = lock;
        playButtons = new ArrayList<>();
        labels = new ArrayList<>();

        try {
            BufferedImage img =ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/other/possible_play_image.png"));
            possiblePlayImage= new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JPanel leftPanel = new JPanel(new BorderLayout());
        this.playerPanel= new PlayerPanel(playerView, true);
        leftPanel.add(playerPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        for (int i = 1; i <= 4; i++) {
            JPanel imagePanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);

            JLabel label = new JLabel();
            if(i==1) {
                if(playerView.objectiveCard!=null) label.setIcon(getImageIcon(GUI.getCardPath(playerView.objectiveCard.getID(), false)));
                else label.setIcon(possiblePlayImage);
            }
            else{
                if(playerView.hand.handCards[i-2]!=null) label.setIcon(getImageIcon(GUI.getCardPath(playerView.hand.handCards[i-2].getID(), false)));
                else label.setIcon(possiblePlayImage);
            }

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 2;
            imagePanel.add(label, gbc);

            if (i > 1 && playerView.hand.handCards[i-2]!=null) {
                //carte della mano
                cardsID[i-2]=playerView.hand.handCards[i-2].getID();
                isFacedown[i-2]=false;
                labels.add(i-2,label);
                // Creazione e aggiunta del bottone Flip
                JButton flipButton = new JButton("Flip");
                final int index = i-2;

                flipButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(cardsID[index]<0) return;
                        isFacedown[index] = !isFacedown[index];
                        playerView.hand.handCards[index].isFacedown=isFacedown[index];
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

    private void showPlayButton() {
        int i=0;
        for(JButton b : playButtons){
            if(cardsID[i]>0) b.setVisible(true);
            i++;
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

    public CardComponent getPlayChoice() {
        return new CardComponent(new StartingCard(choice),playerPanel.getPlayPosition().getRow()-playerPanel.getCenterRow(), playerPanel.getPlayPosition().getCol()-playerPanel.getCenterCol(),0);
    }

    private void confirmPlay(){
        if (playing){
            CardComponent c=playerPanel.getPlayPosition();
            if(selectedLabel!=null && c!=null){
                int i=JOptionPane.showConfirmDialog(this,"Confirm the play of the card N. "+choice+1+" in the position: " + c.getRow()+","+c.getCol()+ " ?\n");
                if(i==0) {//la giocata viene confermata
                    hidePlayButton();
                    selectedLabel.setBorder(new LineBorder(Color.RED, 4));
                    lock.notifyAll();
                }
            }
        }
    }


    protected void update(PlayerView playerView, boolean playing ) {
        this.playing=playing;
        for (int i=0; i<3; i++) {
            //Aggiorno le carte nella mano
            if(playerView.hand.handCards[i]==null){
                cardsID[i]=-1;
                isFacedown[i]=false;
                labels.get(i).setIcon(possiblePlayImage);
            }
            else {
                if (cardsID[i]!=playerView.hand.handCards[i].getID()){
                    cardsID[i]=playerView.hand.handCards[i].getID();
                    isFacedown[i]=false;
                    labels.get(i).setIcon(getImageIcon(GUI.getCardPath(cardsID[i],isFacedown[i])));
                }
            }
        }
        //aggiorno le carte giocate
        playerPanel.update(playerView);
        if (playing) {
            showPlayButton();
        }
    }

    public static void main(String[] args) {
        // Creazione del frame principale
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen

        PersonalPanel panel = new PersonalPanel(new PlayerView(new Player("test", null)), new Object());

        // Aggiunta di JSplitPane al frame
        frame.add(panel);

        // Visualizzazione del frame
        frame.setVisible(true);

        panel.update(null,true);
    }
}
