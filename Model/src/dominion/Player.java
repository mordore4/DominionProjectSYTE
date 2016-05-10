package dominion;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dominion.exceptions.CardNotAvailableException;

import java.util.ArrayList;

/**
 * Created by Sam on 23/03/2016.
 */
public class Player
{
    private Account account;
    private Hand hand;
    private Deck discardPile;
    private Deck deck;
    private int actions;
    private int buys;
    private int coins;
    private GameEngine gameEngine;
    private Game game;

    public Player(GameEngine gameEngine, Game game)
    {
        this.gameEngine = gameEngine;
        this.game = game;

        actions = 1;
        buys = 1;
        coins = 0;

        discardPile = new Deck(false, gameEngine);
        deck = new Deck(true, gameEngine);

        createStartingDeck();

        hand = new Hand(deck, discardPile);
    }

    public void createStartingDeck()
    {
        for (int i = 0; i < 7; i++)
        {
            try
            {
                addCard("copper");
            }
            catch (CardNotAvailableException e)
            {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 3; i++)
        {
            try
            {
                addCard("estate");
            }
            catch (CardNotAvailableException e)
            {
                e.printStackTrace();
            }
        }

        deck.shuffle();
    }

    public void addCard(String cardName) throws CardNotAvailableException
    {
        Card card = game.retrieveCard(cardName);

        if (card.getAmount() > 0)
        {
            card.setAmount(card.getAmount() - 1);

            Card newCard = new Card(card);
            newCard.setAmount(1);

            discardPile.addCard(newCard);
        } else throw new CardNotAvailableException();
    }

    public void buyCard(String cardName) throws CardNotAvailableException
    {
        if (coins >= game.retrieveCard(cardName).getCost())
        {
            addCard(cardName);
            buys--;
        }
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public Account getAccount()
    {
        return account;
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

    public Hand getHand()
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

    public void cleanup()
    {
        ArrayList<Card> currentHand = hand.getCards();

        for (int i = 0; i < currentHand.size(); i++)
        {
            discardPile.addCard(currentHand.get(i));
        }

        hand = new Hand(deck, discardPile);
    }

    public void playCard(String cardName)
    {
        Card currentCard = hand.findCard(cardName);

        if (currentCard.getType() == 1)
        {
            coins += currentCard.getAbilities()[0].getAmount();
        }

        discardPile.addCard(currentCard);
        hand.removeCard(currentCard);
    }
}
