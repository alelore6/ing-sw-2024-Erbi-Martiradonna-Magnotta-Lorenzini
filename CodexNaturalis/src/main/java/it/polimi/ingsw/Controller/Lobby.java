package it.polimi.ingsw.Controller;

public class Lobby {
    protected String[] Nicknames;
    //TODO serve l'attributo numPlayers e il suo setter

    public Lobby() {
        this.Nicknames = new String[4];
    }

    protected boolean addPlayer(String nickname){
        //TODO questo if è sempre falso perchè l'array cosi facendo è sempre lungo 4
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
