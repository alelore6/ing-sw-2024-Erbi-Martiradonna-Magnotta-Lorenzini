package it.polimi.ingsw.Events;

/**
 * Event that represent the successful join of the pre-game lobby of a player
 */
public class JoinLobby extends GenericEvent{

    private final String nickname;
    private final String newNickname;

    /**
     * Constructor
     * @param nickname the player that has joined the lobby
     */
    public JoinLobby(String nickname, String newNickname){
        super("You have joined a lobby, waiting for other players to start the game." +
                "\nPlease, set a password so that you can reconnect to this game in case of " +
                "a disconnection: ", nickname);
        this.nickname = nickname;
        this.newNickname = newNickname;
    }

    @Override
    public String msgOutput() {
        return newNickname == null ? super.msgOutput() : "Since the nickname '" + nickname + "' is already taken," +
                "your new one is " + newNickname + ".\n" + super.msgOutput();
    }

    public String getNewNickname() {
        return newNickname;
    }
}
