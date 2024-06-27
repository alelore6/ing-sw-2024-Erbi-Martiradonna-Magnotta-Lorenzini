package it.polimi.ingsw.Model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.*;

import static it.polimi.ingsw.Model.Position.*;

/**
 * ResourceDeck is the class that represents resource deck in the game, it extends deck because it uses some of the methods and attributes of that class.
 */
public class ResourceDeck extends Deck{
    /**
     * cards in an array of resource cards that composes the gold deck
     */
    private ResourceCard[] cards;


    /**
     * Constructor builds the resource deck, initialize the number of resource cards, then it loads from JSON file resource cards. Every resource card is initialized with four corners and all possible resources.
     */
    public ResourceDeck(){

        NCards = 40;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("assets/data/resource_cards.json");
            if (inputStream == null) {
                throw new FileNotFoundException("File not found!");
            }
            Reader reader = new InputStreamReader(inputStream);

            Gson gson = new Gson();

            cards = gson.fromJson(reader, ResourceCard[].class);

        } catch (IOException e) {
            System.err.println("Error with JSON.");
        }

        for(ResourceCard card : cards){
            card.backCorners = new Corner[4];
            for(int i = 0; i < 4; i++){
                Position pos = null;
                switch (i){
                    case 0 -> pos = UP_SX;
                    case 1 -> pos = UP_DX;
                    case 2 -> pos = DOWN_SX;
                    case 3 -> pos = DOWN_DX;
                }
                card.backCorners[i] = new Corner(pos, null);
            }
        }

        cards = (ResourceCard[]) shuffle(cards);

    }

    /**
     * draw method represents draw action, removing one card from resource deck and returning it.
     * @return the drawn gold card
     * @throws isEmptyException if deck is empty.
     */
    public ResourceCard draw() throws isEmptyException {
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;

            ResourceCard temp = cards[NCards];

            // the resize of the array is an overkill
            cards[NCards] = null;

            // I supposed the drawn card is the one available with the highest index.
            return temp;
        }
    }

    /**
     * peek method is used to know about the color of the first card of the resource deck without drawing it.
     * @return the color of the first card of the deck.
     */
    public CardColor peek() {
        return cards[getNCards()-1].getColor();
    }


}
