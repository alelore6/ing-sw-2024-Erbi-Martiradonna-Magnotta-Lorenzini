package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.*;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Dimension;
import java.util.HashMap;

import static it.polimi.ingsw.Model.TokenColor.*;

/**
 * The panel where the game takes place, you can see both decks (Gold Deck and Resource Deck) and all the  four cards on the floor from where, when the time is right, you can draw, the score track ,as well as  the two community objective cards.
 * It extends JSplitPane to be able to divide the screen between card section and score track section.
 */
public class TableCenterPanel extends JSplitPane {
    /**
     * the game view gives all the required information about the game in any exact moment.
     */
    private GameView gameView;
    /**
     * the list of token colors in the game
     */
    private final ArrayList<TokenColor> chosenColor = new ArrayList<>();
    /**
     * card labels is an array of labels associated to each card spot  associated to a JLabel to visualize through the layout manager each card image that lands on the spot.
     */
    private ArrayList<JLabel> cardLabels;
    /**
     * deck labels is an array of two labels associated to each deck spot associated with a JLabel to visualize through the layout manager each deck peek card image.
     */
    private JLabel[] deckLabels;
    /**
     * An array  of integers that keeps track of the IDs of the cards present in the central spots of the table.
     */
    private int[] cardsID;
    /**
     * an encoding of the draw choice of a player.
     */
    private int drawChoice;
    /**
     * a boolean that sets when is the time to draw for each player.
     */
    private boolean drawing = false;
    /**
     * Spots is an Array of 27 JButton to create 27 spots on score tracks to keep track of each player's current and final score;
     */
    private JButton[] spots;
    /**
     * SpotXCoords is an array of all width coordinates of spots on score tracks to correctly place them on the score track.
     */

    private ArrayList<JButton> drawButtons;
    /**
     * PossiblePlayImage is an image used as a placeholder for spaces where there are no cards available, setting a default image to improve the game's user interface and maintaining visual coherence.
     */
    private ImageIcon possiblePlayImage = null;
    /**
     * DrawLock is used as a synchronization mechanism to ensure that draw card operations in the game are performed in a safe and coordinated manner, preventing potential concurrency issues in a multithreaded environment.
     */
    private final Object drawLock;
    /**
     * tokenImages is an array of images representing every token.
     */
    private Image[] tokenImages;
    /**
     * CENTER_X is the center abscissa of right panel.
     */
    private  int CENTER_X = 0;
    /**
     * CENTER_Y is the center ordinate of right panel.
     */
    private  int CENTER_Y = 0;
    /**
     * START_X is the abscissa where token get drawn,unless being moved by other methods.
     */
    private static int START_X = 100;
    /**
     *START_Y is the ordinate where tokens get drawn,unless being moved by other methods.
     */
    private static int START_Y = 100;
    /**
     * tokenXCoord is a mapping od token colors to their respective abscissas coordinates.
     */
    private HashMap<TokenColor, Integer> tokenXCoords = new HashMap<TokenColor, Integer>();
    /**
     * tokenYCoord is a mapping od token colors to their respective ordinates coordinates.
     */
    private HashMap<TokenColor, Integer> tokenYCoords = new HashMap<TokenColor, Integer>();
    ;
    /**
     * TableCenterPanel constructor builds a new Table Center Panel, configures the interface of the central panel of the game, dividing the space into two main sections (left and right panel)for managing decks and cards, and for viewing the game table with all the tokens required. Initializes the necessary resources, manages panel layouts and sets default images and buttons for user interactions.
     * @param gameView game view associated with all information that TableCenterPanel needs to be created and exploited.
     * @param drawLock the lock object used for synchronization when drawing.
     */

    public TableCenterPanel(GameView gameView, Object drawLock) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;
        this.drawLock = drawLock;
        this.cardLabels = new ArrayList<>();
        this.deckLabels = new JLabel[2];
        this.cardsID = new int[4];
        this.spots = new JButton[5];
        this.tokenImages = new Image[5];
        this.drawButtons = new ArrayList<>();

        for(int i = 0; i < gameView.numPlayers; i++){
            chosenColor.add(gameView.players.get(i).token.color);
        }

        for(TokenColor c : chosenColor){
            tokenXCoords.put(c, START_X);
            tokenYCoords.put(c, START_Y);
        }

        for(String nickname : gameView.tableCenterView.scoreTrack.points.keySet()){
            setNewTokenCoordinates(gameView.tableCenterView.scoreTrack.points.get(nickname), gameView.getPlayerViewByNickname(nickname).token.color);
        }

        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/other/possible_play_image.png"));
            possiblePlayImage = new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));

            for(int i = 0; i < 4; i++){
                if(chosenColor.contains(RED))    tokenImages[0] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_rouge.png"));
                if(chosenColor.contains(YELLOW)) tokenImages[1] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_jaune.png"));
                if(chosenColor.contains(GREEN))  tokenImages[2] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_vert.png"));
                if(chosenColor.contains(BLUE))   tokenImages[3] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_bleu.png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setLeftComponent(createLeftPanel());
        this.setRightComponent(createRightPanel());

        this.setDividerLocation(800);
    }



    /**
     * CreateRightPanel generates the right panel that will compose the Table Center Panel, drawing the score track image where in position 0 all tokens will be generated ready to be used to track each player score.
     * @return the right panel complete of all its features.
     */
    public JPanel createRightPanel() {
        ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/plateau/plateau.png"));
        int originalWidth = img.getIconWidth();
        int originalHeight = img.getIconHeight();
        int width = 900;
        int height = 700;
        double widthProportion = (double) width / originalWidth;
        double heightProportion = (double) height / originalHeight;
        double proportion = Math.min(widthProportion, heightProportion);

        double finalWidth = proportion * originalWidth;
        double finalHeight = proportion * originalHeight;

        Image imgResized = img.getImage().getScaledInstance((int) finalWidth, (int) finalHeight, Image.SCALE_DEFAULT);
        ImageIcon resizedIcon = new ImageIcon(imgResized);
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = (int) ((getWidth() - finalWidth) / 2);
                int y = (int) ((getHeight() - finalHeight) / 2);
                CENTER_X = getWidth()/2;
                CENTER_Y = getHeight()/2;

                g.drawImage(resizedIcon.getImage(), x, y, null);

                for (int i = 0; i < chosenColor.size(); i++) {

                    TokenColor color = null;
                    Image image = null;

                    switch(chosenColor.get(i)){
                        case RED -> {
                            color = RED;
                            image = tokenImages[0];
                        }
                        case YELLOW -> {
                            color = YELLOW;
                            image = tokenImages[1];
                        }
                        case GREEN -> {
                            color = GREEN;
                            image = tokenImages[2];
                        }
                        case BLUE -> {
                            color = BLUE;
                            image = tokenImages[3];
                        }
                    }

                    if(image != null)
                        g.drawImage(image, CENTER_X+tokenXCoords.get(color), CENTER_Y+tokenYCoords.get(color), 30, 30, this);
                }
            }
        };
        rightPanel.setMinimumSize(new Dimension(390, 300));
        /**
         *
         */
        return rightPanel;
    }

    /**
     * setNewTokenCoordinates update the position of a token on the score track after getTokenCordinates indicates where a token must land on score track.
     * @param position actual position of  token.
     * @param color color of the token that must move.
     */
    private void setNewTokenCoordinates(int position, TokenColor color){

        int[] finalCoordinates = getTokenCoordinates(position, color);

        tokenXCoords.replace(color, finalCoordinates[0]);
        tokenYCoords.replace(color, finalCoordinates[1]);
    }

    /**
     *  getTokenCoordinates displays each token position on the score track separating even the position of each different token in the same value in score track.
     * @param position actual position of a token.
     * @param color color of the token that must move.
     * @return new position of the token after a score change.
     */
    private int[] getTokenCoordinates(int position, TokenColor color){
        int X_OFFSET = 0;
        int Y_OFFSET = 0;
        int final_x = 0;
        int final_y = 0;
        final int radius = 10;

        switch(color){
            case RED :
                X_OFFSET = 1;
                Y_OFFSET = -1;
                break;

            case YELLOW :
                X_OFFSET = -1;
                Y_OFFSET = -1;
                break;

            case GREEN :
                X_OFFSET = 1;
                Y_OFFSET = 1;
                break;

            case BLUE :
                X_OFFSET = -1;
                Y_OFFSET = 1;
                break;

            default:
                break;
        }

        switch (position){
            case 0 :
                final_x = -98;
                final_y = 286;
                break;

            case 1 :
                final_x = -15;
                final_y = 286;
                break;

            case 2 :
                final_x = 68;
                final_y = 286;
              break;
            case 3 :
             final_x = 110;
                final_y = 211;
                break;

            case 4 :
                final_x=27;
                final_y= 211;
               break;
            case 5 :
                final_x = -56;
                final_y = 211;
             break;
            case 6 :
                final_x = -139;
                final_y = 211;
                break;
            case 7 :
                final_x =-139;
                final_y = 135;
                break;
            case 8 :
                final_x = -56;
                final_y = 135;
                break;
            case 9 :
                final_x = 27;
                final_y = 135;
                break;
            case 10 :
                final_x = 110;
                final_y = 135;
                break;
            case 11 :
                final_x = 110;
                final_y = 60;
                break;
            case 12 :
                final_x = 27;
                final_y = 60;
                break;
            case 13 :
                final_x = -56;
                final_y = 60;
                break;
            case 14 :
                final_x = -139;
                final_y = 60;
                break;
            case 15 :
                final_x = -139;
                final_y = -15;
                break;
            case 16 :
                final_x = -56;
                final_y = -15;
                break;
            case 17 :
                final_x = 27;
                final_y = -15;
                break;
            case 18 :
                final_x = 110;
                final_y = -15;
                break;
            case 19 :
                final_x = 110;
                final_y = -90;
                break;
            case 20 :
                final_x = -15;
                final_y = -129;
                break;
            case 21 :
                final_x =-139 ;
                final_y = -90;
                break;
            case 22 :
               final_x = -139;
               final_y = -166;
              break;
            case 23 :
                final_x = -139;
                final_y = -242;
                break;
            case 24:
                final_x = -90;
                final_y = -304;
                break;
            case 25 :
                final_x = -14;
                final_y = -314;
                break;
            case 26 :
                final_x = 61;
                final_y = -302;
                break;
            case 27 :
                final_x = 108;
                final_y = -242;
                break;
            case 28 :
                final_x = 109;
                final_y = -166;
                break;
            case 29 :
                final_x = -13;
                final_y = -225;
                break;

        }

        final_x += X_OFFSET * radius;
        final_y += Y_OFFSET * radius;

        return new int[]{final_x, final_y};
    }
    /**
     * createLeftPanel method is crucial for creating the left panel of the game table in the TableCenterPanel class. It manages the organization and addition of card decks, center cards and objective cards, as well as management of draw buttons.
     * @return left panel
     */
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Decks
        addDeck(leftPanel, "Gold Deck", gameView.tableCenterView.topGoldCardColor, 0);
        addDeck(leftPanel, "Resource Deck", gameView.tableCenterView.topResourceCardColor, 1);

        for (int i = 0; i < 4; i++) {
            if (gameView.tableCenterView.centerCards[i] != null) {
                addCardSpot(leftPanel, i, gameView.tableCenterView.centerCards[i].getID());
            } else {
                addCardSpot(leftPanel, i, -1);
            }
        }

        if (gameView.tableCenterView.objCards[0] != null) {
            addObjectiveCardSpot(leftPanel, "Objective Card 1", gameView.tableCenterView.objCards[0].getID());
        } else {
            addObjectiveCardSpot(leftPanel, "Objective Card 1", -1);
        }

        if (gameView.tableCenterView.objCards[1] != null) {
            addObjectiveCardSpot(leftPanel, "Objective Card 2", gameView.tableCenterView.objCards[1].getID());
        } else {
            addObjectiveCardSpot(leftPanel, "Objective Card 2", -1);
        }
        leftPanel.setMinimumSize(new Dimension(800, 300));
        hideDrawButton();
        return leftPanel;
    }

    /**
     * addObjectiveCardSpot adds a spot for an objective card, displaying the card icon or a placeholder if there is no card.
     * @param panel panel that contains sub-panels, to which card panel will be added.
     * @param title title of ObjectiveCardSPot
     * @param cardID ID of ObjectiveCard
     */
    private void addObjectiveCardSpot(JPanel panel, String title, int cardID) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel cardLabel = new JLabel();
        if (cardID > 0) {
            cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));
        } else {
            cardLabel.setIcon(possiblePlayImage);
        }
        cardPanel.add(cardLabel, BorderLayout.CENTER);


        panel.add(cardPanel);
    }

    /**
     * addDeck creates deck panel with a title , and add it to the panel: it adds a deck of cards with the top card icon and a button to draw cards from that deck.
     * @param panel panel that contains sub-panels, to which deck panel will be added.
     * @param title title of deck panel border
     * @param cardColor the color of the back of the card to be displayed.
     * @param deckIndex index of deck, used tp determine the card color ID and array index for labels and buttons.
     */
    public void addDeck(JPanel panel, String title, CardColor cardColor, int deckIndex) {
        JPanel deckPanel = new JPanel();
        deckPanel.setLayout(new BorderLayout());
        deckPanel.setBorder(BorderFactory.createTitledBorder(title));

        JLabel deckLabel = new JLabel();
        try{
            int colorId = getCardColorId(cardColor,deckIndex==0);
            deckLabel.setIcon(getImageIcon(GUI.getCardPath(colorId, true), 0, 0));
        }
        catch(IllegalArgumentException e){
            deckLabel.setIcon(possiblePlayImage);
        }
        deckLabels[deckIndex] = deckLabel;
        deckPanel.add(deckLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.setPreferredSize(new Dimension(75, 10));
        final int index=deckIndex==0? 6: 5;

        drawButton.addActionListener(e -> {
            drawCard(index);
        });
        drawButtons.add(drawButton);
        deckPanel.add(drawButton, BorderLayout.EAST);
        panel.add(deckPanel);
    }

    /**
     * addCardSpot creates a card panel and adds a spot for each central card, displaying the card icon or a placeholder if there is no card, and includes a button to draw the card.
     * @param panel panel that contains sub-panels, to which card panel will be added.
     * @param index index of card spot to determine
     * @param cardID IDs of cards
     */
    private void addCardSpot(JPanel panel, int index, int cardID) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createTitledBorder("Card " + (index + 1)));

        JLabel cardLabel = new JLabel();
        if (cardID > 0) {
            cardLabel.setIcon(getImageIcon(GUI.getCardPath(cardID, false), 0, 0));
        } else {
            cardLabel.setIcon(possiblePlayImage);
        }
        cardLabels.add(cardLabel);
        cardsID[index] = cardID;
        cardPanel.add(cardLabel, BorderLayout.CENTER);

        JButton drawButton = new JButton("Draw");
        drawButton.setPreferredSize(new Dimension(80, 1));
        drawButton.addActionListener(e -> {
            drawCard(index + 1);
        });
        drawButtons.add(drawButton);
        cardPanel.add(drawButton, BorderLayout.EAST);
        panel.add(cardPanel);
    }

    /**
     * getImageIcon method loads an image from the specified path into the application's classpath, resizes it to the desired size, and returns it as an ImageIcon.
     * This method is useful for managing card images and other graphic assets in the game GUI
     * @param path the file path to the image resource.
     * @param scaleX the width to which the image should be scaled.
     * @param scaleY the height to which the image should be scaled.
     * @return ImageIcon scaled.
     */
    private ImageIcon getImageIcon(String path, int scaleX, int scaleY) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_SMOOTH));
    }

    /**
     * drawCard method handles the selection of a card to draw, updating the internal state to reflect the user's choice,
     * hiding the draw buttons, and notifying any waiting threads that a draw action has completed.
     * @param spotID ID of the spot where the draw action must be done.
     */
    private void drawCard(int spotID) {
        if (drawing) {
            this.drawChoice = spotID;
            hideDrawButton();
            synchronized (drawLock) {
                drawLock.notifyAll();
            }
        }
    }

    /**
     * getDrawChoice is used to encode player choice to obtain the index of the player's drawing choice, allowing the system to react based on this choice.
     * @return encode of the draw choice of a generic player.
     */
    public int getDrawChoice() {
        return drawChoice;
    }

    /**
     * UpdateCard  update the card spot information after a draw, to replace the drawn card with a new one.
     */
    private void updateCards() {
        for (int i = 0; i < 4; i++) {
            if (gameView.tableCenterView.centerCards[i] == null) {
                cardLabels.get(i).setIcon(possiblePlayImage);
                cardsID[i] = -1;
            } else if (cardsID[i] != gameView.tableCenterView.centerCards[i].getID()) {
                cardsID[i] = gameView.tableCenterView.centerCards[i].getID();
                cardLabels.get(i).setIcon(getImageIcon(GUI.getCardPath(cardsID[i], false), 0, 0));
            }
        }
    }

    /**
     * UpdateDeck  update the deck spot information after a draw, to replace the drawn card with a new one.
     */
    private void updateDeck() {
        for (int i = 0; i < 2; i++) {
            CardColor deckCardColor;
            if (i == 0) {
                deckCardColor = gameView.tableCenterView.topGoldCardColor;
            } else {
                deckCardColor = gameView.tableCenterView.topResourceCardColor;
            }
            try{
                int colorId = getCardColorId(deckCardColor, i==0);
                deckLabels[i].setIcon(getImageIcon(GUI.getCardPath(colorId, true), 0, 0));
            }
            catch (IllegalArgumentException e ){
                deckLabels[i].setIcon(possiblePlayImage);
            }
        }
    }

    /**
     * showDrawButton shows draw button to make them usable.
     */
    public void showDrawButton() {
        int i=0;
        for (JButton button : drawButtons) {
            if(i<2 || cardsID[i-2]>0) button.setVisible(true);
            i++;
        }
    }

    /**
     * hideDrawButton hides draw button to make them unusable.
     */
    private void hideDrawButton() {
        for (JButton button : drawButtons) {
            button.setVisible(false);
        }
    }

    /**
     * Update method update the entire Tabel Center Panel to keep playing without have to create each time a new panel.
     * @param gameView the game info
     * @param drawing boolean to know
     */
    public void update(GameView gameView, boolean drawing) {
        this.gameView = gameView;
        this.drawing = drawing;
        updateDeck();
        updateCards();

        for(String nickname : gameView.tableCenterView.scoreTrack.points.keySet()){
            setNewTokenCoordinates(gameView.tableCenterView.scoreTrack.points.get(nickname), gameView.getPlayerViewByNickname(nickname).token.color);
        }

        if (drawing) {
            showDrawButton();
        }
        revalidate();
        repaint();
    }

    /**
     * getCardColorId method is essential for mapping card colors to their respective numeric IDs, taking into account whether the cards are gold or resource to facilitate card image retrieval.
     * @param color all type of colors of all cards.
     * @param gold a boolean to distinguish gold card from resource card
     * @return ID encode of a cord from its color and type.
     */
    public static int getCardColorId(CardColor color,boolean gold) {
        int id=0;
        switch (color) {
            case RED:
                id=1; break;
            case GREEN:
                id=11; break;
            case PURPLE:
                id=31; break;
            case BLUE:
                id=21; break;
            default:
                throw new IllegalArgumentException("Unknown CardColor: " + color);
        }
        if (gold) return id+40;
        else return id;
    }

}

