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
        this.setName(name);
        this.setCardList(cardList);
        this.setStarted(false);
        this.setKingdomCards(kingdomCards);

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
        this.setGame(new Game(playerArray, kingdomCards, cardList));
        this.setStarted(true);
    }

    public Game getGame()
    {
        return game;
    }

    public boolean isStarted()
    {
        return started;
    }

    private void setGame(Game game)
    {
        this.game = game;
    }

    private void setName(String name)
    {
        this.name = name;
    }

    private void setKingdomCards(String[] kingdomCards)
    {
        this.kingdomCards = kingdomCards;
    }

    private void setCardList(HashMap<String, Card> cardList)
    {
        this.cardList = cardList;
    }

    private void setStarted(boolean started)
    {
        this.started = started;
    }
}
