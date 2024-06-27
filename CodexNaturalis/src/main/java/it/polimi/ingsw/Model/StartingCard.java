package it.polimi.ingsw.Model;

/**
 * StartingCard is the class that represents the starting cards, that with all the playable cards creates the game board.
 * It extends Card because it uses some of the methods and attributes of that class.
 */
public class StartingCard extends Card{
    /**
     * resources represents all resources of starting cards, it is an array because for certain starting cards there are more than one resource only in the center.
     */
    public Resource[] resource;

    /**
     * Constructor that creates a fake starting card to let the view know where a card can be played and where a card can't be played.
     * @param fake_ID a fake starting card used just to create the spot where a real card can be placed.
     */
    public StartingCard(int fake_ID) {
        this.ID = fake_ID;
        this.playOrder=100;
    }
}