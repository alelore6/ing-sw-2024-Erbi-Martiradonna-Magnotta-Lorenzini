package it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private objectiveCard objective;
    private startingCard startCard;
    private Token token;
    private CurrentResources currentResources;
    private Hand hand;

    Player(String nickname, startingCard startCard, Token token){
        this.nickname=nickname;
        this.startCard=startCard;
        this.token= token;
        currentResources= new CurrentResources();
        hand = new Hand();
    }
    public String getNickname() {
        return nickname;
    }

    public objectiveCard getObjective() {
        return objective;
    }

    public void chooseObjective(objectiveCard obj1, objectiveCard obj2){
        //richiede input utente
    }
    public startingCard getStartcard() {
        return startcard;
    }

    public Token getToken() {
        return token;
    }

}
