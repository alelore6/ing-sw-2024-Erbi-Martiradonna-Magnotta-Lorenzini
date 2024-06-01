package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.ModelView.GameView;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainFrame extends JFrame {

    private JMenuBar menuBar;
    private JMenu menu;
    private JPanel mainPanel;
    private TableCenterPanel tableCenterPanel;
    private PersonalPanel personalPanel;
    private HashMap<String,PlayerPanel> otherPlayers;
    private String nickname;
    private Graphics g;



    public MainFrame(String nickname) {
        super("CodexNaturalis");
        this.nickname = nickname;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        /*setLayout(new BorderLayout());*/

        //TODO non funziona nessuno dei due modi per aggiungere l'immagine
        mainPanel = new JPanel(new BorderLayout()) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
            }
        };

        try {
            ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/rulebook/01.png"));
            int width = 800;
            int height = 720;
            if (img.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.out.println("Errore nel caricamento dell'immagine.");
                return;
            }
            Image imgResized = img.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT);
            ImageIcon resizedIcon = new ImageIcon(imgResized);

            //1
           JPanel initialPanel = new JPanel(new CardLayout()){
              @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    int x = (getWidth() - width) / 2;
                    int y = (getHeight() - height) / 2;
                    g.drawImage(resizedIcon.getImage(), x, y, this);
                }
            };

            mainPanel.add(initialPanel, BorderLayout.CENTER);



        } catch (Exception e) {
            System.out.println("Errore nel caricamento dell'immagine: " + e.getMessage());
            e.printStackTrace();
        }
       // mainPanel.add(new JLabel(centerImgIcon));
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //Game model = new Game(2,new String[]{"1111","2222"}, null);
                MainFrame mainFrame = new MainFrame("Test");
                //frame.reactStartGame(model.clone());

            }
        });
    }





    public void createGamePanels(GameView gameView) {

        tableCenterPanel = new TableCenterPanel(gameView);
        addToMenuBar("Table center");
        mainPanel.getLayout().addLayoutComponent("Table center", tableCenterPanel);

        otherPlayers = new HashMap<String, PlayerPanel>();

        for(int i=0; i<gameView.numPlayers;i++){

            if (gameView.players.get(i).nickname.equalsIgnoreCase(nickname)){
                personalPanel = new PersonalPanel(gameView.players.get(i));
                addToMenuBar("Personal panel");
                mainPanel.getLayout().addLayoutComponent("Personal panel", personalPanel);

            } else {
                otherPlayers.put(gameView.players.get(i).nickname, new PlayerPanel(gameView.players.get(i)));
                addToMenuBar(gameView.players.get(i).nickname+" panel");
                mainPanel.getLayout().addLayoutComponent(gameView.players.get(i).nickname+" panel", tableCenterPanel);
            }
        }

    }


    private void addToMenuBar(String label){

        JMenuItem menuItem = new JMenuItem(label);

        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchPanel(label);
            }
        });

        menu.add(menuItem);

    }

    public void switchPanel(String  panel) {
        CardLayout layout =(CardLayout) (mainPanel.getLayout());
        layout.show(mainPanel, panel);
    }

    public void reactStartGame(GameView gameView){
        this.menuBar=new JMenuBar();
        this.menu = new JMenu("Panels");
        createGamePanels(gameView);
        switchPanel("tableCenterPanel");
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        this.setVisible(true);
    }

   private JPanel getPanelByLabel(String label){
        if(label.equalsIgnoreCase("Table center")) return tableCenterPanel;
        else if(label.equalsIgnoreCase("Personal panel")) return personalPanel;
        else return otherPlayers.get(label);
    }

    public void update(GameView gameView){
        tableCenterPanel.update(gameView);
        personalPanel.update(gameView.getPlayerViewByNickname(nickname));

        for(String name:otherPlayers.keySet()){
            otherPlayers.get(name).update(gameView.getPlayerViewByNickname(name));
        }
    }

}
