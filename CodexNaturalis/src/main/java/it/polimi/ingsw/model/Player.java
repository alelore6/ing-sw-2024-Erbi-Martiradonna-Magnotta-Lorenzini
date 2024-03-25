package it.polimi.ingsw.model;

public class Player {

    private final String nickname;
    private ObjectiveCard objective;
    private StartingCard startCard;
    private Token token;
    private final CurrentResources currentResources;
    private final Hand Hand;

    private final Game game;
    private boolean isFirst;
    //ha più senso ordinare l'array di players in game con l'ordine di gioco

    Player(String nickname, Game game){
        this.nickname=nickname;
        this.game = game;
        currentResources= new CurrentResources(this);
        Hand = new Hand(this);
        isFirst = false;
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

    public void PlaceStartingCard(){
        //possibilità di rotare carta da input
        //TODO settare currentResources con le risorse di startCard
        //perchè per come è implementato ora non posso passare una startingCard a update
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

    public void setisFirst() {
        isFirst = true;
    }


    public Game getGame() {
        return game;
    }

    public void setStartCard(StartingCard startCard) {
        this.startCard = startCard;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
