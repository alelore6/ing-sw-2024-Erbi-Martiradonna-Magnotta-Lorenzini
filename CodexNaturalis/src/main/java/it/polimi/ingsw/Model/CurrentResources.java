package it.polimi.ingsw.Model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class that contains the information about the resources owned by the player
 */
public class CurrentResources implements Serializable {
    /**
     * the player that owns the class
     */
    private final Player player;
    /**
     * the map that connects every resource to relative number owned by the player
     */
    protected final HashMap<Resource,Integer> currentResources;

    /**
     * Constructor: initialize all resources values to 0
     * @param player the player that owns the resources
     */
     CurrentResources(Player player) {
         currentResources = new HashMap<Resource, Integer>();
         this.player = player;
         for (Resource r : Resource.values()) {
             currentResources.put(r, 0);
         }
     }

    public HashMap<Resource, Integer> getCurrentResources() {
        return currentResources;
    }

    /**
     * update the player's resources after every card played:
     * 1. remove overlapped corners resources
     * 2. add card resources
     * 3. calculate card points
     * @param card the card that has just been played
     * @param overlaps the corners that have been covered
     */
     protected void update(Card card, Corner[] overlaps){
         //remove overlapped corners resources
         for (Corner c: overlaps){
             if (c!=null && c.getResource() !=null){
                 Resource r = c.getResource();
                 if(currentResources.get(r)>0)
                     currentResources.replace(r,currentResources.get(r)-1);
             }
         }
         // face down cards are different
         if(card.isFacedown){
             if(card instanceof StartingCard){
                 //add back corners resources
                for(int i=4; i<8; i++){
                    if (card.backCorners[i].pos!=null && card.backCorners[i].getResource()!=null){
                        Resource r=card.backCorners[i].getResource();
                        currentResources.replace(r,currentResources.get(r)+1);
                    }
                }
                //add central resources for starting card
                 for(Resource t : ((StartingCard) card).resource){
                     currentResources.replace(t,currentResources.get(t)+1);
                 }
             }
             else {
                 //add central resources for resource and gold card
                 switch (((PlayableCard) card).getColor()){
                     case RED -> currentResources.replace(Resource.FUNGI,currentResources.get(Resource.FUNGI)+1);
                     case GREEN -> currentResources.replace(Resource.PLANT,currentResources.get(Resource.PLANT)+1);
                     case PURPLE -> currentResources.replace(Resource.INSECT,currentResources.get(Resource.INSECT)+1);
                     case BLUE -> currentResources.replace(Resource.ANIMAL,currentResources.get(Resource.ANIMAL)+1);
                 }
             }
         } else {
             // add new card's front resources
             for(int j=0; j<card.frontCorners.length; j++){
                 if (card.frontCorners[j].pos!=null && card.frontCorners[j].getResource()!=null){
                     Resource temp= card.frontCorners[j].getResource();
                     currentResources.replace(temp,currentResources.get(temp)+1);
                 }
             }
         }
        if (!(card instanceof StartingCard)){
            int addPoints=((PlayableCard) card).getPoints();
            if (card instanceof GoldCard){
                if (((GoldCard) card).getRPoints()!=null){
                    Resource g=((GoldCard) card).getRPoints();
                    addPoints *= currentResources.get(g);
                }else if(((GoldCard) card).isRPointsCorner()){
                    int count=0;
                    for(Corner c: overlaps){
                        if (c!=null){
                            count++;
                        }
                    }
                    addPoints *= count;
                }
            }
            player.getToken().move(addPoints);
        }
     }
}
