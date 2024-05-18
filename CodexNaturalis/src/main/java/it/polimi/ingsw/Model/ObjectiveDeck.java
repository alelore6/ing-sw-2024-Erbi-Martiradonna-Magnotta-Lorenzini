package it.polimi.ingsw.Model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ObjectiveDeck {
    //Non implenta deck perché il metodo draw() di deck ritorna una Card, ObjectiveCard però non estende Card.
    //TODO dovrebbe essere sistemata

    private int NCards;
    private ObjectiveCard[] cards;

    private ObjectiveCard1[] temp_deck1;
    private ObjectiveCard2[] temp_deck2;

    public ObjectiveDeck() {
        NCards = 16;
        Gson gson = new Gson();

        try{
            FileReader reader1 = new FileReader("src/main/resources/assets/data/objective_cards_1.json");
            FileReader reader2 = new FileReader("src/main/resources/assets/data/objective_cards_2.json");

            temp_deck1 = gson.fromJson(reader1, ObjectiveCard1[].class);
            temp_deck2 = gson.fromJson(reader2, ObjectiveCard2[].class);
        } catch (IOException e) {
            // This is only for debugging: it'll be removed later.
            System.out.println("FILE not found!");
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