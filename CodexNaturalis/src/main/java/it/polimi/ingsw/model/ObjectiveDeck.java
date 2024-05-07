package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectiveDeck{

    private int NCards;
    private ObjectiveCard[] cards;

    public ObjectiveDeck() {
        NCards = 16;

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/data/resource_card.json")) {

            cards = gson.fromJson(reader, ObjectiveCard[].class);

        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE non trovato!");

        }

        List<ObjectiveCard> cardList = Arrays.asList(cards);

        Collections.shuffle(cardList);

        cardList.toArray(cards);

    }

    public ObjectiveCard draw(){

        NCards--;

        // I supposed the drawn card is the one available with the highest index.
        return cards[NCards];

        // WARNING! We don't delete the card from the array doing this.
    }
}