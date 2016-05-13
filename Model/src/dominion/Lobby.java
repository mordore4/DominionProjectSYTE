package dominion;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Digaly on 24/03/2016.
 */
public class Lobby
{
    private ArrayList<String> playersInLobby;
    private Game game;
    private String name;
    private String password;
    private String cardSet;
    private HashMap<String, Card> cardList;
    private boolean started;

    public Lobby(String playerName, String name, String password, HashMap<String, Card> cardList)
    {
        this.name = name;
        this.password = password;
        this.cardList = cardList;
        this.started = false;

        playersInLobby = new ArrayList<String>();
        playersInLobby.add(playerName);

        cardSet = "testWitch";
    }

    public void addPlayer(String playerName)
    {
        if (playersInLobby.size() < 4)
        {
            playersInLobby.add(playerName);
        }
    }

    public String getPlayer(String username)
    {
        String player = null;

        for (String a : playersInLobby)
        {
            if (a.equals(username))
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
        String[] playerArray = playersInLobby.toArray(new String[playersInLobby.size()]);
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
