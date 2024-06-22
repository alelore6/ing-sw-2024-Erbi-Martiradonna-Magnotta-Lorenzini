package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MainFrame extends JFrame {
    private GUI gui;
    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JPanel backgroundPanel;
    private TableCenterPanel tableCenterPanel;
    public PersonalPanel myPanel;
    private ChatPanel chatPanel;
    private HashMap<String, PlayerPanel> otherPlayers;
    private String nickname;
    private ImageIcon icon;
    private JPanel temp;
    private Object playLock=new Object();

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

            backgroundPanel = new JPanel() {
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


    private void createGamePanels(GameView gameView) {
        tableCenterPanel = new TableCenterPanel(gameView,playLock);
        addToMenuBar("Table center");
        mainPanel.add(tableCenterPanel, "Table center");
        addToMenuBar("My panel");

        otherPlayers = new HashMap<String, PlayerPanel>();

        for (int i = 0; i < gameView.numPlayers; i++) {
            if (gameView.players.get(i).nickname.equalsIgnoreCase(nickname)) {
                myPanel= new PersonalPanel(gameView.players.get(i), playLock);
                mainPanel.add(myPanel, "My panel");
            } else {
                PlayerPanel playerPanel = new PlayerPanel(gameView.players.get(i), false);
                otherPlayers.put(gameView.players.get(i).nickname, playerPanel);
                addToMenuBar(gameView.players.get(i).nickname + "'s panel");
                mainPanel.add(playerPanel, gameView.players.get(i).nickname + "'s panel");
            }
        }
    }

    private void addToMenuBar(String label) {
        JMenuItem menuItem = new JMenuItem(label);

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


    public void switchPanel(String label) {
        CardLayout layout = (CardLayout) (mainPanel.getLayout());
        layout.show(mainPanel, label);
        mainPanel.repaint();
        mainPanel.revalidate();
        //System.out.println("Switching panel: " + label);
    }

    public void reactStartGame(GameView gameView) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                menuBar = new JMenuBar();
                menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
                createGamePanels(gameView);
                setJMenuBar(menuBar);
                revalidate();
                switchPanel("Table center");
            }});
    }


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

    public String showInputDialog(String title, String message, Object[] possibilities) {
        return (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE, icon, possibilities, null);
    }

    public void showMessageDialog(String message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        JOptionPane.showMessageDialog(mainPanel,message);}});
    }

    public void addChatMessage(String nickname, String message){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
                chatPanel.addChatMessage(message,nickname);
//            }
//        });
    }

    protected void sendChatMessage(String message){
        gui.sendChatMessage(message);
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getLock(){
        return playLock;
    }

    public CardComponent getPlayChoice(){
        return myPanel.getPlayChoice();
    }

    public int getDrawChoice(){
        return tableCenterPanel.getDrawChoice();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game model = new Game(4, new String[]{"1111", "2222", "3333", "4444"}, null);
                MainFrame mainFrame = new MainFrame(null);
                mainFrame.setNickname("2222");
                mainFrame.reactStartGame(model.clone());
                mainFrame.addChatMessage("test", "test");
                mainFrame.addChatMessage("game", "test");

            }
        });
    }

}
