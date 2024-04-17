package it.polimi.ingsw.model;

/**
 * Class that contains information about the player
 */
public class Player {

    private final String nickname;
    private ObjectiveCard objective;
    private Token token;
    private final CurrentResources currentResources;
    private final Hand Hand;
    public final Game game;
    protected int position;

    /**
     * Constructor
     * @param nickname the player's nickname
     * @param game the game the player is in
     */
    Player(String nickname, Game game){
        this.nickname=nickname;
        this.game = game;
        currentResources= new CurrentResources(this);
        Hand = new Hand(this);
    }

    /**
     * Getter for player's nickname
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for player's secret objective card
     * @return player's secret objective card
     */
    public ObjectiveCard getObjective() {
        return objective;
    }

    /**
     * Setter for player's secret objective card, the player must choose one between obj1 and obj2
     * @param obj1 the random objective card the player can choose
     * @param obj2 the random objective card the player can choose
     */
    public void chooseObjective(ObjectiveCard obj1, ObjectiveCard obj2){
        //TODO client's input request
    }

    /**
     * Play the starting card in the central position,before playing the card can be rotated
     * @param StartCard the player's random starting card
     * @throws WrongPlayException is the play is not valid
     */
    protected void PlaceStartingCard(StartingCard StartCard) throws WrongPlayException {
        //TODO Card can be rotated from the player
        Hand.playCard(StartCard,40,40);
    }

    /**
     * Getter for player's token
     * @return player's token
     */
    public Token getToken() {
        return token;
    }

    /**
     * Getter for player's current resources
     * @return player's current resources object
     */
    public CurrentResources getCurrentResources() {
        return currentResources;
    }

    /**
     * Getter for player's hand and played cards
     * @return player's Hand object
     */
    public Hand getHand() {
        return Hand;
    }

    /**
     * Setter for player's token after choosing the color
     * @param token the chosen token
     */
    public void setToken(Token token) {
        this.token = token;
    }
}
