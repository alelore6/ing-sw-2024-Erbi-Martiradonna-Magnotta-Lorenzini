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

/**
 * Class that represent the panel of the player that owns the frame
 */
public class PersonalPanel extends JSplitPane {
    /**
     * nickname of the owner of the frame
     */
    protected final String nickname;
    /**
     * the scroll pane that contains the player's played cards
     */
    private final PlayerPanel playerPanel;
    /**
     * indicates the chosen card to play from the hand
     */
    private int choice;
    /**
     * list of buttons that make the play
     */
    private final ArrayList<JButton> playButtons;
    /**
     * list of JLabel containing the images of the hand cards
     */
    private final ArrayList<JLabel> labels;
    /**
     * the id of the cards in the hand of the player
     */
    private final int[] cardsID= new int[3];
    /**
     * keep traces of the flipping actions on the hand cards
     */
    private final boolean[] isFacedown= new boolean[3];
    /**
     * represent the chosen card to play from the hand
     */
    private JLabel selectedLabel;
    /**
     * indicates whether it's the player's turn to play
     */
    private boolean playing = false;
    /**
     * the image for empty card slot
     */
    private ImageIcon possiblePlayImage =null;
    /**
     * lock for the synchronization of the play actions
     */
    private final Object lock;

    /**
     * Constructor: creates a split pane with the player's played cards and his hand cards
     * @param playerView the player info based on which the panel is build
     * @param lock lock for the synchronization of the play actions
     */
    PersonalPanel(PlayerView playerView, Object lock) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.nickname = playerView.nickname;
        this.lock = lock;
        this.playButtons = new ArrayList<>();
        this.labels = new ArrayList<>();

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
                labels.add(label);
                // Creazione e aggiunta del bottone Flip
                JButton flipButton = new JButton("Flip");
                final int index = i-2;

                flipButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(cardsID[index]<0) return;
                        isFacedown[index] = !isFacedown[index];
                        ///playerView.hand.handCards[index].isFacedown=isFacedown[index];
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

        setDividerLocation(820); // Imposta la posizione iniziale del divisore

        // Impostazione della larghezza minima del pannello
        rightPanel.setMinimumSize(new Dimension(390, 300));
    }

    /**
     * show the play buttons next to the cards to allow the selection
     */
    private void showPlayButton() {
        int i=0;
        for(JButton b : playButtons){
            if(cardsID[i]>0) b.setVisible(true);
            i++;
        }
    }

    /**
     * hide the play button next to the cards when it's not the player's turn
     */
    private void hidePlayButton() {
        for(JButton b : playButtons){
            b.setVisible(false);
        }
    }

    /**
     * load the cards image
     * @param path the path to the image
     * @return the card image
     */
    private ImageIcon getImageIcon(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));
    }

    /**
     * Getter for play action wrapped in a card component class
     * @return the card component representing the play
     */
    public CardComponent getPlayChoice() {
        CardComponent c=new CardComponent(new StartingCard(choice),playerPanel.getPlayPosition().getRow()-playerPanel.getCenterRow(), playerPanel.getPlayPosition().getCol()-playerPanel.getCenterCol(),0);
        c.setFlipped(isFacedown[choice]);
        return c;
    }

    /**
     * allows the player to confirm the play action through a confirm dialog after checking both position and hand are chosen.
     * Also check that it's the player's turn with the playing attribute
     */
    private void confirmPlay(){
        if (playing){
            CardComponent c=playerPanel.getPlayPosition();
            if(selectedLabel!=null && c!=null){
                int i=JOptionPane.showConfirmDialog(this,"Confirm the play of the card N. "+choice+1+" in the position: " + c.getRow()+","+c.getCol()+ " ?\n");
                if(i==0) {//la giocata viene confermata
                    hidePlayButton();
                    selectedLabel.setBorder(new LineBorder(Color.RED, 4));
                    synchronized (lock){lock.notifyAll();}
                }
            } else JOptionPane.showMessageDialog(this,"Remember to select a position where to play the card, then click on the chosen card play button");
        }
    }

    /**
     * Updates the panel with the new info. Also calls the update method on his player panel.
     * @param playerView the updated player info
     * @param playing indicates whether it's the player's turn. If true calls the show play button method.
     */
    protected void update(PlayerView playerView, boolean playing ) {
        this.playing=playing;
        if (playing) {
            showPlayButton();
        }
        if(playerView==null) return;

        if (selectedLabel!=null) selectedLabel.setBorder(null);
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

    }


}
