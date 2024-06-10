package it.polimi.ingsw.Graphical;

import it.polimi.ingsw.Model.Card;

import java.awt.image.BufferedImage;

class CardComponent {
    private final Card card;
    private BufferedImage image;
    private final int row;
    private final int col;
    private final int positionOrder;

    public CardComponent(Card card, int row, int col, int positionOrder) {
        this.card = card;
        this.row = row;
        this.col = col;
        this.positionOrder = positionOrder;
    }

    public Card getCard() {
        return card;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getPositionOrder() {
        return positionOrder;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
