package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.FileReader;
import java.io.IOException;

public class GoldDeck extends Deck{

    private GoldCard[] cards;

    public GoldDeck(){

        NCards = 40;

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/data/resource_card.json")) {

            cards = gson.fromJson(reader, GoldCard[].class);

        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE non trovato!");

        }

        cards = (GoldCard[]) shuffle(cards);
    }

    public GoldCard draw() throws isEmptyException {
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;

            // I supposed the drawn card is the one available with the highest index.
            return cards[NCards];
        }
        // WARNING! We don't delete the card from the array doing this.
    }
}
