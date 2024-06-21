package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exceptions.WrongPlayException;

/**
 * Class that contains player's information and game items
 */
public class Player {
    /**
     * player's nickname, acts as identifier for the player
     */
    private final String nickname;
    /**
     * player's secret objective card
     */
    private ObjectiveCard objective;
    /**
     * player's token that will be positioned in table center
     */
    private Token token;
    /**
     * player's current resources
     */
    private final CurrentResources currentResources;
    /**
     * player's hand and played cards
     */
    private final Hand Hand;
    /**
     * the game the player is participating
     */
    public Game game;
    /**
     * indicate when it's this player's turn
     */
    protected int position;

    protected volatile boolean isDisconnected;

    /**
     * Constructor:
     * Also calls the constructor for class Hand and currentResources
     *
     * @param nickname the player's nickname
     */
    public Player(String nickname, Game game){
        this.nickname=nickname;
        this.game = game;
        currentResources= new CurrentResources(this);
        Hand = new Hand(this);
        isDisconnected = false;
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
     * @return player's objective card
     */
    public ObjectiveCard getObjective() {
        return objective;
    }

    /**
     * Setter for player's secret objective card, the player must choose one between obj1 and obj2
     * @param objCard the objective card the player has chosen
     */
    public void chooseObjective(ObjectiveCard objCard){
        this.objective=objCard;
    }

    /**
     * Play the starting card in the central position,
     * before playing, the card can be rotated by the player
     * @param StartCard the player's random starting card
     * @throws WrongPlayException is the play is not valid
     */
    public void placeStartingCard(StartingCard StartCard) throws WrongPlayException {
        Hand.playCard(StartCard,40,40);
    }

    /**
     * Getter for player's token
     * @return player's token object
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
     * @param color the token's color that the player has chosen
     */
    public boolean setToken(TokenColor color) {
        synchronized (game.availableTokens) {
            //non so se serve veramente
            if(game.availableTokens.remove(color)){
                this.token = new Token(color,game.tablecenter.getScoretrack(),this);
                return true;
            }
        }
        return false;
    }
}
