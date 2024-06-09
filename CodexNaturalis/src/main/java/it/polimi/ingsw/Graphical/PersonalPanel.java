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

public class PersonalPanel extends JSplitPane {
    protected final String nickname;
    private final PlayerView playerView;
    private int choice;
    private JButton playButton1;
    private JButton playButton2;
    private JButton playButton3;
    private JLabel selectedLabel;

    PersonalPanel(PlayerView playerView) {
        super(JSplitPane.HORIZONTAL_SPLIT);
        this.nickname = playerView.nickname;
        this.playerView = playerView;

        JPanel leftPanel = new JPanel(); // questo sarebbe playedcards panel
        leftPanel.setBackground(Color.decode("#d9dbc1"));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        for (int i = 1; i <= 4; i++) {
            JPanel imagePanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);

            // Creazione e aggiunta dell'immagine
            JLabel label = new JLabel();
            int k = ((i - 1) * 25) + 1;
            label.setIcon(getImageIcon(GUI.getCardPath(k, false)));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 2; // Span di due righe
            imagePanel.add(label, gbc);

            if (i != 1) {
                // Creazione e aggiunta del bottone Flip
                JButton flipButton = new JButton("Flip");
                final int index = i;
                final boolean[] isFacedown = new boolean[3];
                for (int j = 0; j < 3; j++) {
                    isFacedown[j] = false;
                }
                final int cardID = k;
                flipButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //devo anche girare la carta veramente
                        isFacedown[index - 2] = !isFacedown[index - 2];
                        label.setIcon(getImageIcon(GUI.getCardPath(k, isFacedown[index - 2])));
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
                        choice = index - 2;
                        System.out.println("You have chosen card N. "+ (choice+1));
                        if (selectedLabel != null) {
                            selectedLabel.setBorder(null); // Rimuove il bordo dalla label precedente
                        }
                        selectedLabel = label;
                        Border border = new LineBorder(Color.GREEN, 4); // Crea un bordo rosso
                        selectedLabel.setBorder(border); // Imposta il bordo sulla label selezionata
                    }
                });
                if (i == 2) playButton1 = playButton;
                else if (i == 3) playButton2 = playButton;
                else playButton3 = playButton;
                gbc.gridy = 1;
                imagePanel.add(playButton, gbc);
            }

            rightPanel.add(imagePanel);
            rightPanel.add(Box.createVerticalStrut(5)); // Riduce lo spazio verticale tra le carte
        }

        //hidePlayButton();
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
    }

    private void showPlayButton() {
        playButton1.setVisible(true);
        playButton2.setVisible(true);
        playButton3.setVisible(true);
    }

    private void hidePlayButton() {
        playButton1.setVisible(false);
        playButton2.setVisible(false);
        playButton3.setVisible(false);
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

    protected void update(PlayerView playerView, boolean play ) {
        if (play) showPlayButton();
    }
}
