package dominion;

import java.util.ArrayList;

/**
 * Created by Digaly on 24/03/2016.
 */
public class Lobby
{
    private ArrayList<Account> playersInLobby;
    private Game game;
    private String name;
    private String password;
    private String cardSet;
    private GameEngine gameEngine;


    public Lobby(Account account, String name, String password, GameEngine gameEngine)
    {
        playersInLobby.add(account);
        this.name = name;
        this.password = password;
        cardSet = "default";
        this.gameEngine = gameEngine;
    }

    public void addPlayer(Account account)
    {
        if (playersInLobby.size() < 4)
        {
            playersInLobby.add(account);
        }
    }

    public String getName()
    {
        return name;
    }

    public void startGame()
    {
        Account[] playerArray = (Account[]) playersInLobby.toArray();
        game = new Game(playerArray, cardSet, gameEngine);
    }

}
