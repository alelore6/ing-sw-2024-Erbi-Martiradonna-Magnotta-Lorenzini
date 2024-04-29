package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.Exceptions.isEmptyException;

import java.io.FileReader;
import java.io.IOException;

public class ResourceDeck extends Deck{

    private ResourceCard[] cards;

    public ResourceDeck(){

        NCards = 40;

        Gson gson = new Gson();

        try (FileReader reader = new FileReader("src/main/resources/assets/cards/resource_card.json")) {

            cards = gson.fromJson(reader, ResourceCard[].class);

        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE non trovato!");

            // TODO: add the catch action
        }

        cards = (ResourceCard[]) shuffle(cards);

    }
    public ResourceCard draw() throws isEmptyException {
        if(getNCards() == 0){
            throw new isEmptyException(this);
        }
        else {
            NCards--;
            return cards[NCards];
        }
        // WARNING! We don't delete the card from the array doing this.
    }
}
