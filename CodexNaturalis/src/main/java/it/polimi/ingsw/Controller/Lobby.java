package it.polimi.ingsw.Controller;

public class Lobby {
    protected String[] Nicknames;

    public Lobby() {
        this.Nicknames = new String[4];
    }

    protected boolean addPlayer(String nickname){

        if(Nicknames.length < 4){
            Nicknames[Nicknames.length] = nickname;

            return true;
        }

        return false;
    }

    public int getNumPlayers(){
        return Nicknames.length;
    }
}
