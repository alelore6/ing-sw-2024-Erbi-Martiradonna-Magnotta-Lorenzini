package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectiveDeck {
    //Non implenta deck perché il metodo draw() di deck ritorna una Card, ObjectiveCard però non estende Card.
    //TODO dovrebbe essere sistemata

    private int NCards;
    private ObjectiveCard[] cards;

    public ObjectiveDeck() {
        NCards = 16;
        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/data/objective_cards.json")) {
            cards = gson.fromJson(reader, ObjectiveCard[].class);

        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE not found!");
        }

        assert cards != null;
        List<ObjectiveCard> cardList = Arrays.asList(cards);
        Collections.shuffle(cardList);
        cardList.toArray(cards);
    }

    public ObjectiveCard draw(){
        if (NCards == 0) {
            throw new RuntimeException(); //SHOULDN'T NEVER HAPPEN
        }
        else{
            NCards--;
            // I supposed the drawn card is the one available with the highest index.
            return cards[NCards];
        }

        // I supposed the drawn card is the one available with the highest index.


        // WARNING! We don't delete the card from the array doing this.
    }
}