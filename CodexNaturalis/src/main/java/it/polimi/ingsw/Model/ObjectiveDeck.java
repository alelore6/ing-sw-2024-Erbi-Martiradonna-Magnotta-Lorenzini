package it.polimi.ingsw.Model;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectiveDeck {
    //Non implenta deck perché il metodo draw() di deck ritorna una Card, ObjectiveCard però non estende Card.


    private int NCards;
    private ObjectiveCard[] cards;

    private ObjectiveCard1[] temp_deck1;
    private ObjectiveCard2[] temp_deck2;

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

        } catch (IOException e) {
            System.out.println("Error with JSON.");
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