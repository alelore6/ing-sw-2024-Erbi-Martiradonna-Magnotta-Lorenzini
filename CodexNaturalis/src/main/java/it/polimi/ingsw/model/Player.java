package it.polimi.ingsw.model;

public class Player {

    private final String nickname;
    private ObjectiveCard objective;
    private Token token;
    private final CurrentResources currentResources;
    private final Hand Hand;
    public final Game game;
    protected int position;

    Player(String nickname, Game game){
        this.nickname=nickname;
        this.game = game;
        currentResources= new CurrentResources(this);
        Hand = new Hand(this);
    }
    public String getNickname() {
        return nickname;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }

    public void chooseObjective(ObjectiveCard obj1, ObjectiveCard obj2){
        //TODO client's input request
    }

    protected void PlaceStartingCard(StartingCard StartCard) throws WrongPlayException {
        //TODO Card can be rotated from client's view
        Hand.playCard(StartCard,40,40);
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

    public void setToken(Token token) {
        this.token = token;
    }
}
