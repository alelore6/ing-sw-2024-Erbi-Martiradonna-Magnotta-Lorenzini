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
class TableCenterPanel extends JSplitPane {
    /**
     * the game view gives all the required information about the game in any exact moment.
     */
    private GameView gameView;

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

    private JPanel rightPanel;
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

    private Image[] pawnImages;
    private  int CENTER_X = 0;
    private  int CENTER_Y = 0;
    private static int START_X = 100;
    private static int START_Y = 100;
    private HashMap<TokenColor, Integer> pawnXCoords = new HashMap<TokenColor, Integer>(); //
    private HashMap<TokenColor, Integer> pawnYCoords = new HashMap<TokenColor, Integer>();
    ;
    /**
     * TableCenterPanel constructor configures the interface of the central panel of the game, dividing the space into two main sections for managing decks and cards, and for viewing the game table. Initializes the necessary resources, manages panel layouts and sets default images and buttons for user interaction.
     * @param gameView
     * @param drawLock
     */

    public TableCenterPanel(GameView gameView, Object drawLock) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.gameView = gameView;
        this.drawLock = drawLock;
        this.cardLabels = new ArrayList<>();
        this.deckLabels = new JLabel[2];
        this.cardsID = new int[4];
        this.spots = new JButton[5];
        this.pawnImages = new Image[5];
        this.drawButtons = new ArrayList<>();

        for(int i = 0; i < gameView.numPlayers; i++){
            chosenColor.add(gameView.players.get(i).token.color);
        }

        for(TokenColor c : chosenColor){
            pawnXCoords.put(c, START_X);
            pawnYCoords.put(c, START_Y);
        }

        for(String nickname : gameView.tableCenterView.scoreTrack.points.keySet()){
            setNewTokenCoordinates(gameView.tableCenterView.scoreTrack.points.get(nickname), gameView.getPlayerViewByNickname(nickname).token.color);
        }

        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/other/possible_play_image.png"));
            possiblePlayImage = new ImageIcon(img.getScaledInstance(300, 180, Image.SCALE_DEFAULT));

            for(int i = 0; i < 4; i++){
                if(chosenColor.contains(RED))    pawnImages[0] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_rouge.png"));
                if(chosenColor.contains(YELLOW)) pawnImages[1] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_jaune.png"));
                if(chosenColor.contains(GREEN))  pawnImages[2] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_vert.png"));
                if(chosenColor.contains(BLUE))   pawnImages[3] = ImageIO.read(this.getClass().getClassLoader().getResource("assets/images/tokens/CODEX_pion_bleu.png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.setLeftComponent(createLeftPanel());
        this.setRightComponent(createRightPanel());

        this.setDividerLocation(800);
    }



    /**
     * CreateRightPanel generates the right panel that will compose the Table Center Panel, drawing the score track image with each spot created using JButtons to allow token to land on them to keep track of points.
     * @return
     */
    private JPanel createRightPanel() {
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
//                g.drawImage(pawnImages[0], CENTER_X, CENTER_Y, 50, 50, null);
//                System.out.println(CENTER_X+" "+CENTER_Y);
                for (int i = 0; i < chosenColor.size(); i++) {

                    TokenColor color = null;
                    Image image = null;

                    switch(chosenColor.get(i)){
                        case RED -> {
                            color = RED;
                            image = pawnImages[0];
                        }
                        case YELLOW -> {
                            color = YELLOW;
                            image = pawnImages[1];
                        }
                        case GREEN -> {
                            color = GREEN;
                            image = pawnImages[2];
                        }
                        case BLUE -> {
                            color = BLUE;
                            image = pawnImages[3];
                        }
                    }

                    if(image != null)
                        g.drawImage(image, CENTER_X+pawnXCoords.get(color), CENTER_Y+pawnYCoords.get(color), 30, 30, this);
                }
            }
        };
        rightPanel.setMinimumSize(new Dimension(390, 300));
        this.rightPanel=rightPanel;
        return rightPanel;
    }

    private void setNewTokenCoordinates(int position, TokenColor color){

        int[] finalCoordinates = getTokenCoordinates(position, color);

        pawnXCoords.replace(color, finalCoordinates[0]);
        pawnYCoords.replace(color, finalCoordinates[1]);
    }

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
     * @return
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
     * addObjectiveCardSpot Adds a spot for an objective card, displaying the card icon or a placeholder if there is no card.
     * @param panel
     * @param title
     * @param cardID
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
     * addDeck method adds a deck of cards with the top card icon and a button to draw cards from that deck.
     * @param panel
     * @param title
     * @param cardColor
     * @param deckIndex
     */
    private void addDeck(JPanel panel, String title, CardColor cardColor, int deckIndex) {
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
     * addCardSpot adds a spot for a central card, displaying the card icon or a placeholder if there is no card, and includes a button to draw the card.
     * @param panel
     * @param index
     * @param cardID
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
     * @param path
     * @param scaleX
     * @param scaleY
     * @return
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
     * @param spotID
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
     * @return
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
    private void showDrawButton() {
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
     * @param gameView
     * @param drawing
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
     * @param color
     * @param gold
     * @return
     */
    private static int getCardColorId(CardColor color,boolean gold) {
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

    //TODO: da rimuovere.
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // full screen
        String[] playerNames = {"1", "2", "3", "4"};
        Game game = new Game(4, playerNames, null);
        game.players.get(0).setToken(RED);
        game.players.get(1).setToken(YELLOW);
        game.players.get(2).setToken(GREEN);
        game.players.get(3).setToken(BLUE);
        int x=30;
        game.players.get(0).getToken().move(x);
        game.players.get(1).getToken().move(x);
        game.players.get(2).getToken().move(x);
        game.players.get(3).getToken().move(x);

        GameView gameView = new GameView(game);


        TableCenterPanel panel = new TableCenterPanel(gameView, new Object());
        mainFrame.mainPanel.add(panel, "Table center");
        mainFrame.switchPanel("Table center");

        mainFrame.setVisible(true);
    }
}

