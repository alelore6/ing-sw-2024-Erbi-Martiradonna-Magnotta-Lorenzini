package main.java.it.polimi.ingsw.model;

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
        if(scoreTrackPos == 29 || (scoreTrackPos + points) > 29){  //se supera o si trova all'ultima posizione
            getScoretrack().getTokenPos()[scoreTrackPos] -= 0;
            getScoretrack().getTokenPos()[29] += 1;
            scoreTrackPos = 29;
        }
        else if((scoreTrackPos + points) <= 29) {  //altrimenti vado alla nuova posizione
            getScoretrack().getTokenPos()[scoreTrackPos] -= 0;
            getScoretrack().getTokenPos()[scoreTrackPos + points] += 1;
            scoreTrackPos = scoreTrackPos + points;
        }


    }
}

