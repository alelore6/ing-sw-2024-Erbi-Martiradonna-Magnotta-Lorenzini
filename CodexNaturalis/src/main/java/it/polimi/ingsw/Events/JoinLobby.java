package it.polimi.ingsw.Events;

/**
 * Event that represent the successful join of the pre-game lobby of a player
 */
public class JoinLobby extends GenericRequest{

    private final String newNickname;

    /**
     * Constructor
     * @param oldNickname the player that has joined the lobby
     */
    public JoinLobby(String oldNickname, String newNickname){
        super("You have joined a lobby, waiting for other players to start the game...\n" +
                "Set a password so that you can reconnect to this game in case of disconnection. \n (min. 4 characters and no space allowed):", oldNickname);
        this.newNickname = newNickname;
    }

    @Override
    public String msgOutput(){
        return newNickname.equals(nickname) ? super.msgOutput() : "\u001B[4m" + "Since the nickname '" + nickname + "' is already taken," +
                "your new one is " + "\u001B[1m" + newNickname + "\u001B[0m" + ".\n" + super.msgOutput();
    }

    public String getNewNickname(){
        return newNickname;
    }
}
