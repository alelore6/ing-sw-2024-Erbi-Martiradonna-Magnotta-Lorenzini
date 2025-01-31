package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.View.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class that represent a dialog that allows to choose between two shown images
 */
public class ImageDialog extends JDialog {
    /**
     * represent the chosen image
     */
    private int choice;

    /**
     * Constructor: creates the dialog with 2 images and the relative choice buttons
     * @param parent the container of the dialog
     * @param message the message shown at the top of the dialog
     * @param cardPath1 the path to the first image
     * @param cardPath2 the path to the second image
     * @param useFront decides whether the label for the images are front/back or Card 1/Card2
     * @throws IOException if images cant be loaded
     */
    public ImageDialog(Frame parent, String message, String cardPath1, String cardPath2, boolean useFront) throws IOException {
        super(parent, "Choose Image", true);
        setLayout(new BorderLayout());
        choice = 0;
        // Message Label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Load the images from provided paths: Front is the first one
        BufferedImage img1 = ImageIO.read(this.getClass().getClassLoader().getResource(cardPath1));
        BufferedImage img2 = ImageIO.read(this.getClass().getClassLoader().getResource(cardPath2));

        JLabel imageLabel1 = new JLabel(new ImageIcon(img1.getScaledInstance(400, 240, Image.SCALE_DEFAULT)));
        JLabel imageLabel2 = new JLabel(new ImageIcon(img2.getScaledInstance(400, 240, Image.SCALE_DEFAULT)));

        JLabel label1;
        JLabel label2;
        if (!useFront) {
            label1 = new JLabel("Card 1  ");
            label2 = new JLabel("Card 2  ");
        } else {
            label1 = new JLabel("Front  ");
            label2 = new JLabel("Back  ");
        }

        JPanel imagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 10); // Spazio inferiore tra le etichette e le immagini
        imagePanel.add(label1, gbc);

        gbc.gridx = 1;
        imagePanel.add(imageLabel1, gbc);

        gbc.gridx = 2;
        JButton button1 = new JButton("Choose");
        imagePanel.add(button1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        imagePanel.add(label2, gbc);

        gbc.gridx = 1;
        imagePanel.add(imageLabel2, gbc);

        gbc.gridx = 2;
        JButton button2 = new JButton("Choose");
        imagePanel.add(button2, gbc);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = 1;
                dispose();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = 2;
                dispose();
            }
        });

        // Aggiungi il messaggio in alto
        add(messagePanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);

        setSize(750, 600);
        setLocationRelativeTo(parent);
    }

    /**
     * Getter for the chosen image
     * @return an int representing the chosen image
     */
    public int getChoice() {
        return choice;
    }

}
