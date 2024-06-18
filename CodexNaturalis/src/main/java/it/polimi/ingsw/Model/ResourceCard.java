package it.polimi.ingsw.Model;

public class ResourceCard extends PlayableCard{

    // Just for testing.
    public ResourceCard(int fake_ID, int playOrder){
        this.ID=fake_ID;
        isFacedown=false;
        this.playOrder=playOrder;
    }
}
