package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.View.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class PlayedCardsPanel extends JPanel {
    //panel che permette la sovrapposizione degli angoli delle carte
    private final int overlapOffset = 40;
    private int[][] matrix;
    private final int size;


    public PlayedCardsPanel(int[][] matrix) {
        this.matrix = matrix;
        size = matrix[0].length;
        setLayout(null); // Layout manager nullo per posizionamento assoluto

        //aggiungo le carte
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);

                if (matrix[i][j] != 0) {
                    label.setIcon(getImageIcon(GUI.getCardPath(matrix[i][j],false)));
                }

                add(label);
            }
        }
    }

    private static final int MATRIX_SIZE = 10; //solo per test

    public static void main(String[] args) {

        int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];

        // Riempimento della matrice con valori casuali per esempio
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if(i%2==j%2) matrix[i][j] = rand.nextInt(20);
                else matrix[i][j] = 0;
            }
        }
        JFrame frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PlayedCardsPanel panel = new PlayedCardsPanel(matrix);
        PlayerPanel scrollPane = new PlayerPanel(null,panel);


        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }




    private ImageIcon getImageIcon(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ImageIcon(img.getScaledInstance(250, 150, Image.SCALE_DEFAULT));
    }




        @Override
    public void doLayout() {
        int numComponents = getComponentCount();
        int rowCount = (int) Math.sqrt(numComponents);
        int colCount = rowCount;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                int index = i * colCount + j;
                if (index < numComponents) {
                    Component comp = getComponent(index);
                    int x = j * (comp.getPreferredSize().width - overlapOffset);
                    int y = i * (comp.getPreferredSize().height - overlapOffset);
                    comp.setBounds(x, y, comp.getPreferredSize().width, comp.getPreferredSize().height);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (getComponentCount() > 0) {
            int rowCount = size;
            int colCount = size;
            int componentWidth = getComponent(0).getPreferredSize().width;
            int componentHeight = getComponent(0).getPreferredSize().height;
            int preferredWidth = (colCount - 1) * (componentWidth - overlapOffset) + componentWidth;
            int preferredHeight = (rowCount - 1) * (componentHeight - overlapOffset) + componentHeight;
            return new Dimension(preferredWidth, preferredHeight);
        } else {
            return super.getPreferredSize();
        }
    }
}

