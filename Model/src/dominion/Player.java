package dominion;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dominion.exceptions.CardNotAvailableException;

import java.util.ArrayList;

/**
 * Created by Sam on 23/03/2016.
 */
public class Player
{
    private String name;
    private Deck hand;
    private Deck discardPile;
    private Deck deck;
    private int actions;
    private int buys;
    private int coins;

    public Player()
    {
        actions = 1;
        buys = 1;
        coins = 0;

        discardPile = new Deck();
        deck = new Deck();
        hand = new Deck();

        //hand.makeHand(deck, discardPile);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public Deck getDeck()
    {
        return deck;
    }

    public Deck getDiscardPile()
    {
        return discardPile;
    }

    public int getActions()
    {
        return actions;
    }

    public int getBuys()
    {
        return buys;
    }

    public int getCoins()
    {
        return coins;
    }

    public Deck getHand()
    {
        return hand;
    }

    public void setCoins(int coins)
    {
        this.coins = coins;
    }

    public void setBuys(int buys)
    {
        this.buys = buys;
    }

    public void setActions(int actions)
    {
        this.actions = actions;
    }

    public boolean hasActionCards()
    {
        return hand.containsActionCards();
    }

    public boolean isMyTurn(String nickname)
    {
        return this.name.equals(nickname);
    }
}
