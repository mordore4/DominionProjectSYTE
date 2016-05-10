package dominion;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Deck
{
    private ArrayList<Card> cards;
    private int victoryPoints;
    private GameEngine gameEngine;

    public Deck(Boolean isNewDeck, GameEngine gameEngine)
    {
        this.gameEngine = gameEngine;
        cards = new ArrayList<Card>();

        /*if (isNewDeck)
        {
            Card copper = new Card(gameEngine.findCard("copper"));
            copper.setAmount(1);
            Card estate = new Card(gameEngine.findCard("estate"));
            estate.setAmount(1);

            for (int i = 0; i < 7; i++)
            {
                cards.add(copper);
            }
            for (int i = 0; i < 3; i++)
            {
                cards.add(estate);
            }
            shuffle();
        }*/
    }

    public Card takeTopCard(Deck discardPile)
    {
        if (cards.size() == 0)
        {
            discardToDeck(discardPile);
        }

        Card topCard = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);

        return topCard;
    }

    public Card getCard(String name)
    {
        Card card = null;

        for (Card c : cards)
        {
            if (c.getName().equals(name))
                card = c;
        }

        return card;
    }

    public ArrayList<Card> getCards()
    {
        return cards;
    }

    public int getVictoryPoints()
    {
        return victoryPoints;
    }

    public void calculatePoints()
    {
        int points = 0;
        for (int cardnr = 0; cardnr < cards.size(); cardnr++)
        {
            //points += cards[cardnr].getValue();
        }

        victoryPoints = points;
    }

    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    public void discardToDeck(Deck discardPile)
    {
        ArrayList<Card> temp = discardPile.cards;
        discardPile.cards = this.cards;
        this.cards = temp;
        shuffle();
    }

    public int size()
    {
        return cards.size();
    }

    public String toString()
    {
        String out = "";

        for (Card c : cards)
        {
            out += c.toString() + "\n";
        }

        return out;
    }

    public void addCard(Card card)
    {
        cards.add(card);
    }

}
