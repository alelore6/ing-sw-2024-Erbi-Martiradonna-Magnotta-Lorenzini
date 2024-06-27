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
     * Constructor
     * @param fake_ID
     */
    public StartingCard(int fake_ID) {
        this.ID = fake_ID;
        this.playOrder=100;
    }
}