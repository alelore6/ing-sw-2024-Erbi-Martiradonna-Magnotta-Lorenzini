package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.ModelView.GameView;
import it.polimi.ingsw.View.GUI;


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
    public JPanel mainPanel;
    private TableCenterPanel tableCenterPanel;
    private PersonalPanel personalPanel;
    private HashMap<String,PlayerPanel> otherPlayers;
    private String nickname;
    private ImageIcon icon;

    public MainFrame(String nickname) {
        super("CodexNaturalis");
        this.nickname = nickname;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //full screen
        /*setLayout(new BorderLayout());*/



        try {
            ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("assets/images/rulebook/01.png"));
            int width = 800;
            int height = 720;

            Image imgResized = img.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT);
            ImageIcon resizedIcon = new ImageIcon(imgResized);

            //icon for dialogs
            Image icon = img.getImage().getScaledInstance(35, 35, Image.SCALE_DEFAULT);
            this.icon=new ImageIcon(icon);

           mainPanel = new JPanel(){
              @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    int x = (getWidth() - width) / 2;
                    int y = (getHeight() - height) / 2;
                    g.drawImage(resizedIcon.getImage(), x, y, this);
                }
            };

        } catch (Exception e) {
            System.out.println("Errore nel caricamento dell'immagine: " + e.getMessage());
            e.printStackTrace();
        }
        add(mainPanel, BorderLayout.CENTER);
//        mainPanel.revalidate();
//        mainPanel.repaint();

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game model = new Game(2,new String[]{"1111","2222"}, null);
                MainFrame mainFrame = new MainFrame("1111");
                mainFrame.reactStartGame(model.clone());

                String s= mainFrame.showDialog("test","messaggio",null);

                System.out.println(s);


            }
        });
    }





    private void createGamePanels(GameView gameView) {

        tableCenterPanel = new TableCenterPanel(gameView);
        addToMenuBar("Table center");
        mainPanel.getLayout().addLayoutComponent(null, tableCenterPanel);

        otherPlayers = new HashMap<String, PlayerPanel>();

        for(int i=0; i<gameView.numPlayers;i++){

            if (gameView.players.get(i).nickname.equalsIgnoreCase(nickname)){
                personalPanel = new PersonalPanel(gameView.players.get(i));
                addToMenuBar("Personal panel");
                mainPanel.getLayout().addLayoutComponent(null, personalPanel);

            } else {
                otherPlayers.put(gameView.players.get(i).nickname, new PlayerPanel(gameView.players.get(i)));
                addToMenuBar(gameView.players.get(i).nickname+"'s panel");
                mainPanel.getLayout().addLayoutComponent(null, tableCenterPanel);
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

        menuBar.add(menuItem);

    }

    public void switchPanel(String  panel) {
        //CardLayout layout =(CardLayout) (mainPanel.getLayout());
        //layout.show(mainPanel, panel);
        System.out.println("Switching panel: " + panel);
    }

    public void reactStartGame(GameView gameView){
        this.menuBar=new JMenuBar();
        createGamePanels(gameView);
        switchPanel("Table center");
        this.setJMenuBar(menuBar);
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

    public String showDialog(String title, String message, Object[] possibilities){
        return (String) JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE, icon, possibilities, null);
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

}
