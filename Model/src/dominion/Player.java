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
        this.setActions(1);
        this.setBuys(1);
        this.setCoins(0);

        discardPile = new Deck();
        deck = new Deck();
        hand = new Deck();

        //hand.makeHand(deck, discardPile);
    }

    public void addCards(int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            if (discardPile.size() + deck.size() != 0)
            {
                deck.takeTopCard(hand, discardPile);
            }
        }
    }

    public void addActions(int amount)
    {
        actions += amount;
    }

    public void addBuys(int amount)
    {
        buys += amount;
    }

    public void addCoins(int amount)
    {
        coins += amount;
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
