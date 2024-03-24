package it.polimi.ingsw.model;

public class Player {

    private final String nickname;
    private ObjectiveCard objective;
    private final StartingCard startCard;
    private final Token token;
    private final CurrentResources currentResources;
    private final Hand Hand;

    Player(String nickname, StartingCard startCard, Token token){
        this.nickname=nickname;
        this.startCard=startCard;
        this.token= token;
        currentResources= new CurrentResources();
        Hand = new Hand();
        PlaceStartingCard();
    }
    public String getNickname() {
        return nickname;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }

    public void chooseObjective(ObjectiveCard obj1, ObjectiveCard obj2){
        //richiede input utente
    }
    public StartingCard getStartcard() {
        return startcard;
    }

    public void PlaceStartingCard(){
        //possibilità di rotare carta
        // settare manualmente currentResources con le risorse di startCard
    }

    public Token getToken() {
        return token;
    }

    public CurrentResources getCurrentResources() {
        return currentResources;
    }

    public Hand getHand() {
        return Hand;
    }

    public StartingCard getStartCard() {
        return startCard;
    }
}
