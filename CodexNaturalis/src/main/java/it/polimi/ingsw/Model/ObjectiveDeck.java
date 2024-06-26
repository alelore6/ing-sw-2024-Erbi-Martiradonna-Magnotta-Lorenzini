package it.polimi.ingsw.Model;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ObjectiveDeck is the class that represents the Deck that contains all the objective cards.
 * As opposed to the other deck classes, it doesn't implement Deck class because it returns  a Card, but ObjectiveCard doesn't extend Card.
 */
public class ObjectiveDeck {
    /**
     * NCards is the integer to track the number of cards of the objective deck.
     */
    private int NCards;
    /**
     *The cards field represents the main deck of all 16 objective cards in the ObjectiveDeck class.
     */
    private ObjectiveCard[] cards = new ObjectiveCard[16];
    /**
     * temp_deck1 is an array that memorizes objective card type 1 loaded from JSON.
     */
    private ObjectiveCard1[] temp_deck1;
    /**
     * temp_deck2 is an array that memorizes objective card type 2 loaded from JSON.
     */
    private ObjectiveCard2[] temp_deck2;

    /**
     * Constructor initializes number of cards, load cards data from JSON,deserializes JSON data into arrays of ObjectiveCard1 and ObjectiveCard 2 objects.
     * Then initialize resource maps for ObjectiveCard2, prepares and shuffles the main deck of cards with the loaded cards.
     */
    public ObjectiveDeck() {
        NCards = 16;

        try {
            InputStream inputStream1 = getClass().getClassLoader().getResourceAsStream("assets/data/objective_cards_1.json");
            InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("assets/data/objective_cards_2.json");
            if (inputStream1 == null || inputStream2 == null) {
                throw new FileNotFoundException("File not found!");
            }
            Reader reader1 = new InputStreamReader(inputStream1);
            Reader reader2 = new InputStreamReader(inputStream2);

            Gson gson = new Gson();

            temp_deck1 = gson.fromJson(reader1, ObjectiveCard1[].class);
            temp_deck2 = gson.fromJson(reader2, ObjectiveCard2[].class);

            for(ObjectiveCard2 card : temp_deck2){
                for (Resource resource : Resource.values()) {
                    if(!card.getReqMap().containsKey(resource))
                        card.addReqMap(resource, 0);
                }
            }

        } catch (IOException e) {
            System.err.println("Error with JSON.");
        }

        assert temp_deck1 != null;
        assert temp_deck2 != null;

        List<ObjectiveCard> cardList = new ArrayList<>();

        // 8 is the number of the objective's type x cards. (x == 1 or 2)
        for(int i = 0; i < 8; i++){
            cardList.add(temp_deck1[i]);
            cardList.add(temp_deck2[i]);
        }

        Collections.shuffle(cardList);
        cardList.toArray(cards);
    }

    /**
     * Draw method is used to draw to allow to have it in the hand of the player who carries it out.
     * @return one of the cards loaded from JSON
     */
    public ObjectiveCard draw(){
        if (NCards == 0) {
            throw new RuntimeException(); //SHOULDN'T NEVER HAPPEN
        }
        else{
            NCards--;

            ObjectiveCard temp = cards[NCards];

            // the resize of the array is an overkill
            cards[NCards] = null;

            // I supposed the drawn card is the one available with the highest index.
            return temp;
        }
    }
}