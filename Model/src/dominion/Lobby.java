package dominion;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, Card> cardList;
    private boolean started;

    public Lobby(Account account, String name, String password, HashMap<String, Card> cardList)
    {
        this.name = name;
        this.password = password;
        this.cardList = cardList;
        this.started = false;

        playersInLobby = new ArrayList<Account>();
        playersInLobby.add(account);

        cardSet = "default";
    }

    public void addPlayer(Account account)
    {
        if (playersInLobby.size() < 4)
        {
            playersInLobby.add(account);
        }
    }

    public Account getPlayer(String username)
    {
        Account player = null;

        for (Account a : playersInLobby)
        {
            if (a.getName().equals(username))
                player = a;
        }

        return player;
    }

    public String getName()
    {
        return name;
    }

    public void startGame()
    {
        Account[] playerArray = playersInLobby.toArray(new Account[playersInLobby.size()]);
        game = new Game(playerArray, cardSet, cardList);
        started = true;
    }

    public Game getGame()
    {
        return game;
    }

    public boolean isStarted()
    {
        return started;
    }
}
