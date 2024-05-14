package it.polimi.ingsw.Model;

/**
 * Class containing the token object used to move on the score track
 */
public class Token {

    /**
     * color of the Token
     */
    private TokenColor color;
    /**
     * token's current position on the score track
     */
    private int scoreTrackPos;
    /**
     * score track instance on which the token is set in
     */
    private Scoretrack scoretrack;
    /**
     * player owning the token
     */
    private Player player;

    /**
     * Constructor: sets the token attributes
     * @param color passed by user's input
     * @param scoretrack scoretrack on which the token is set
     * @param player player that will own this token
     */
    public Token(TokenColor color, Scoretrack scoretrack, Player player) {
        this.color = color;
        this.scoreTrackPos = 0;
        this.scoretrack = scoretrack;
        this.player = player;
        scoretrack.addToken(this);
    }

    /**
     * getter for the token's color
     * @return token's color
     */
    public TokenColor getColor() {
        return color;
    }

    /**
     * getter for the score track position
     * @return token's position
     */
    public int getScoreTrackPos() {
        return scoreTrackPos;
    }

    /**
     * getter for the score track instance
     * @return the score track instance
     */
    public Scoretrack getScoretrack() {
        return scoretrack;
    }

    /**
     * getter for the player owning the token
     * @return player instance owning the token
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Moves the token along the score track accordingly equal to the number of points passed by parameter,
     * making sure not to pass the 29 points threshold.
     * @param points points done in that turn by the player's owning the token
     */
    public void move(int points){
        if(scoreTrackPos == 29 || (scoreTrackPos + points) > 29){  //if currentpos is 29 or goes over 29 sets position at 29
            scoretrack.getTokenPos()[scoreTrackPos] -= 1;
            scoretrack.getTokenPos()[29] += 1;
            scoreTrackPos = 29; //update attribute
        }
        else if((scoreTrackPos + points) <= 29) {  //if new move does not go all the way to 29, sets new position
            scoretrack.getTokenPos()[scoreTrackPos] -= 1;
            scoretrack.getTokenPos()[scoreTrackPos + points] += 1;
            scoreTrackPos = scoreTrackPos + points;
        }
        scoretrack.move(this,scoreTrackPos);
        if(getScoretrack().getTokenPos()[scoreTrackPos] >=20){ //endgame if 20 points are reached
            player.game.endGame(player.position);
        }


    }

}

