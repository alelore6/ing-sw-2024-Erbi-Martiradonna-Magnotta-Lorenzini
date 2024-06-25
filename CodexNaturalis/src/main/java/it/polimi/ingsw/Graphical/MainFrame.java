package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.TokenColor;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *  represent the frame for the graphic UI.
 *  It contains all the panels and the switch logic.
 */
public class MainFrame extends JFrame {
    /**
     * the UI that communicates with the frame
     */
    private final GUI gui;
    /**
     * the menu bar that allows the switch between panels
     */
    private JMenuBar menuBar;
    /**
     * panel that contains all the game panels
     */
    private final JPanel mainPanel;
    /**
     * the panel that contains the table center information and logic
     */
    private TableCenterPanel tableCenterPanel;
    /**
     * the panel of the owner of the frame
     */
    public PersonalPanel myPanel;
    /**
     * the panel that contains the chat and its logic
     */
    private final ChatPanel chatPanel;
    /**
     * map between other player's nicknames and their panel
     */
    private HashMap<String, PlayerPanel> otherPlayers;
    /**
     * the owner of the frame nickname
     */
    private String nickname;
    /**
     * the logo shown in dialogs
     */
    private ImageIcon icon;
    /**
     * lock that allows the synchronization for game actions
     */
    private final Object playLock=new Object();

    /**
     * Constructor: create the split pane with the chat panel and the default panel.
     * @param gui the UI that communicates with the frame
     */
    public MainFrame(GUI gui) {
        super("CodexNaturalis");
        this.gui=gui;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);// full screen


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
       this.chatPanel=new ChatPanel(this);
        splitPane.add(chatPanel, JSplitPane.LEFT);
        splitPane.setDividerLocation(300); // Imposta la posizione iniziale del divisore

        mainPanel = new JPanel(new CardLayout());
        try {
            ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/rulebook/01.png"));
            int width = 800;
            int height = 720;

            Image imgResized = img.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
            ImageIcon resizedIcon = new ImageIcon(imgResized);

            // icon for dialogs
            Image icon = img.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
            this.icon = new ImageIcon(icon);

            JPanel backgroundPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    int x = (getWidth() - width) / 2;
                    int y = (getHeight() - height) / 2;
                    g.drawImage(resizedIcon.getImage(), x, y, this);
                }
            };
            mainPanel.add(backgroundPanel, "Background");
        } catch (Exception e) {
            System.out.println("Errore nel caricamento dell'immagine: " + e.getMessage());
            e.printStackTrace();
        }
        splitPane.add(mainPanel, JSplitPane.RIGHT);
        add(splitPane, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Creates the actual game panels calling the relative constructor and add the relative menu item by calling the addToMenuBar method.
     * @param gameView the game info based on which the panels are created.
     */
    private void createGamePanels(GameView gameView) {
        tableCenterPanel = new TableCenterPanel(gameView,playLock);
        addToMenuBar("Table center", null);
        mainPanel.add(tableCenterPanel, "Table center");
        addToMenuBar("My panel", gameView.getPlayerViewByNickname(nickname).token.color);

        otherPlayers = new HashMap<String, PlayerPanel>();

        for (int i = 0; i < gameView.numPlayers; i++) {
            if (gameView.players.get(i).nickname.equalsIgnoreCase(nickname)) {
                myPanel= new PersonalPanel(gameView.players.get(i), playLock);
                mainPanel.add(myPanel, "My panel");
            } else {
                PlayerPanel playerPanel = new PlayerPanel(gameView.players.get(i), false);
                otherPlayers.put(gameView.players.get(i).nickname, playerPanel);
                addToMenuBar(gameView.players.get(i).nickname + "'s panel", gameView.players.get(i).token.color);
                mainPanel.add(playerPanel, gameView.players.get(i).nickname + "'s panel");
            }
        }
    }

    /**
     *  add a menu item to the menu bar
     * @param label the label of the menu item
     * @param color the color of the menu item
     */
    private void addToMenuBar(String label, TokenColor color) {
        JMenuItem menuItem = new JMenuItem(label);

        if(color!=null){
            switch (color) {
                case RED : menuItem.setBackground(Color.RED); break;
                case GREEN : menuItem.setBackground(Color.GREEN); break;
                case BLUE : menuItem.setBackground(Color.decode("#256fe6")); break;
                case YELLOW : menuItem.setBackground(Color.YELLOW); break;
            }
        }

        menuItem.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        menuItem.setMargin(new Insets(5, 10, 5, 10));

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(label);
            }
        });

        menuBar.add(menuItem);
    }

    /**
     * allows the switch between different game panels using CardLayout
     * @param label the name of the selected panel
     */
    public void switchPanel(String label) {
        CardLayout layout = (CardLayout) (mainPanel.getLayout());
        layout.show(mainPanel, label);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    /**
     * Creates the game panels by calling the createGamePanels method and the menu bar.
     * @param gameView the game info based on which the panels are created.
     */
    public void reactStartGame(GameView gameView) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                menuBar = new JMenuBar();
                menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
                createGamePanels(gameView);
                setJMenuBar(menuBar);
                String[] nicknames = new String[gameView.numPlayers-1];
                int index = 0;
                for (int i = 0; i < gameView.numPlayers; i++) {
                    if (!gameView.players.get(i).nickname.equalsIgnoreCase(nickname)) {
                     nicknames[index] = gameView.players.get(i).nickname;
                     index ++;
                    }
                }
                chatPanel.createRecipient(nicknames);
                revalidate();
                switchPanel("Table center");
            }});
    }

    /**
     * updates the game panels based on the gameview info.
     * @param gameView contains all the info about the game in a certain moment, like a screenshot.
     * @param playPhase indicates the turn phase for the owner of the frame: 1 for play phase, 2 for draw phase, 0 else.
     */
    public void update(GameView gameView, int playPhase) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                switch (playPhase) {
                    case 1: //playing
                        tableCenterPanel.update(gameView, false);
                        myPanel.update(gameView.getPlayerViewByNickname(nickname), true);
                        switchPanel("My panel");
                        break;
                    case 2: //drawing
                        tableCenterPanel.update(gameView, true);
                        //switchPanel("Table center");
                        break;
                    default:
                        tableCenterPanel.update(gameView, false);
                        myPanel.update(gameView.getPlayerViewByNickname(nickname), false);
                        break;
                }

                for (String name : otherPlayers.keySet()) {
                    otherPlayers.get(name).update(gameView.getPlayerViewByNickname(name));
                }
            }});
    }

    /**
     * show an input dialog in this frame.
     * @param title the title of the dialog.
     * @param message the message shown in the dialog.
     * @param possibilities the possibilities of choices for the input (null for free text area).
     * @return the input as a string.
     */
    public String showInputDialog(String title, String message, Object[] possibilities) {
        return (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE, icon, possibilities, null);
    }

    /**
     * reports the chat message to the chat panel where it will be added.
     * @param nickname the sender of the chat message.
     * @param message the message text.
     */
    public void addChatMessage(String nickname, String message){
        chatPanel.addChatMessage(message,nickname);
    }

    /**
     * reports the sending of a chat message to the gui element.
     * @param message the message text.
     */
    protected void sendChatMessage(String message){
        gui.sendChatMessage(message);
    }

    /**
     * reports the sending of a chat message to only one of the players to the gui element.
     * @param message
     * @param recipient names who you want to send a message to.
     */
    public void sendPrivateChatMessage(String message, String recipient) {
        gui.sendPrivateChatMessage(message,recipient);
    }

    /**
     * sets the nickname of the owner of the frame.
     * @param nickname the nickname of the owner of the frame
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * getter for lock for synchronization.
     * @return the lock object.
     */
    public Object getLock(){
        return playLock;
    }
    /**
     * transmitter for the play action from the personal panel.
     * @return the encoded information for the play.
     */
    public CardComponent getPlayChoice(){
        return myPanel.getPlayChoice();
    }

    /**
     * transmitter for the draw choice from the table center panel.
     * @return the encoded position for the draw.
     */
    public int getDrawChoice(){
        return tableCenterPanel.getDrawChoice();
    }



}
