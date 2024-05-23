package it.polimi.ingsw.Events;

/**
 * Event that represent the successful join of the pre-game lobby of a player
 */
public class JoinLobby extends GenericRequest{

    private final String nickname;
    private final String oldNickname;

    /**
     * Constructor
     * @param nickname the player that has joined the lobby
     */
    public JoinLobby(String nickname, String oldNickname){
        super("You have joined a lobby, waiting for other players to start the game." +
                "\nSet a password so that you can reconnect to this game in case of disconnection:\n" +
                "At least 4 characters and no space allowed.", nickname);
        this.nickname = nickname;
        this.oldNickname = oldNickname;
    }

    @Override
    public String msgOutput() {
        return oldNickname.equals(nickname) ? super.msgOutput() : "Since the nickname '" + oldNickname + "' is already taken," +
                "your new one is " + nickname + ".\n" + super.msgOutput();
    }

    public String getNickname() {
        return nickname;
    }
}
