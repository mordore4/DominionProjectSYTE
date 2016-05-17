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
    private String[] kingdomCards;
    private HashMap<String, Card> cardList;
    private boolean started;

    public Lobby(String playerName, String name, String[] kingdomCards, HashMap<String, Card> cardList)
    {
        this.name = name;
        this.cardList = cardList;
        this.started = false;
        this.kingdomCards = kingdomCards;

        playersInLobby = new ArrayList<>();
        playersInLobby.add(playerName);
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
        game = new Game(playerArray, kingdomCards, cardList);
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
