package it.polimi.ingsw.Model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  Class containing the score track on which the tokens will be moving
 */
public class Scoretrack {
    /**
     * array containing all the token positions for the current game
     */
    private int[] tokenPos;
    /**
     * map that connect every token to his position on the table
     */
    public HashMap<Token,Integer> points;

    /**
     * Constructor: initializes array representing all the 30 positions of the score track (from 0 to 29)
     */
    Scoretrack() {
        tokenPos = new int[30];
        points = new HashMap<Token, Integer>();
    }

    /**
     * getter for the token positions array
     * @return token positions array
     */
    int[] getTokenPos() {
        return tokenPos;
    }

    /**
     * add a token to the scoretrack
     * @param token the player's token
     */
    void addToken(Token token) {
        points.put(token,0); //TODO Per cambiare punti iniziali modificare qui
    }

    /**
     * move the token on the scoretrack
     * @param token the player's token
     * @param point updated points
     */
    void move(Token token,int point) {
        points.replace(token,point);
    }

    /**
     * Create a sorted map between players' nicknames and their points.
     * @return the sorted map between players' nicknames and their points.
     */
    public HashMap<String,Integer> getRankings() {
        HashMap<String,Integer> p = new HashMap<String,Integer>();
        for (Token token : points.keySet()) {
            p.put(token.getPlayer().getNickname(),points.get(token));
        }

        // This is for sorting the hash map
        return p.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}
