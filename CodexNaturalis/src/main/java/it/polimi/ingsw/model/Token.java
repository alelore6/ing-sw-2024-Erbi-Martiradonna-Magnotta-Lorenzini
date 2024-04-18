package it.polimi.ingsw.model;

public class Token {

    private int color;
    private int scoreTrackPos;

    private Scoretrack scoretrack;

    private Player player;
    public Token(int color, Scoretrack scoretrack, Player player) {
        this.color = color;
        this.scoreTrackPos = 0;
        this.scoretrack = scoretrack;
        this.player = player;

    }

    public int getColor() {
        return color;
    }

    public int getScoreTrackPos() {
        return scoreTrackPos;
    }

    public Scoretrack getScoretrack() {
        return scoretrack;
    }

    public Player getPlayer() {
        return player;
    }

    public void move(int points){
        if(scoreTrackPos == 29 || (scoreTrackPos + points) > 29){  //if currentpos is 29 or goes over 29 sets position at 29
            getScoretrack().getTokenPos()[scoreTrackPos] -= 0;
            getScoretrack().getTokenPos()[29] += 1;
            scoreTrackPos = 29; //update attribute
        }
        else if((scoreTrackPos + points) <= 29) {  //if new move does not go all the way to 29, sets new position
            getScoretrack().getTokenPos()[scoreTrackPos] -= 0;
            getScoretrack().getTokenPos()[scoreTrackPos + points] += 1;
            scoreTrackPos = scoreTrackPos + points;
        }

        if(getScoretrack().getTokenPos()[scoreTrackPos] >=20){ //endgame if 20 points are reached
            player.game.endGame(player.position);
        }


    }
}

