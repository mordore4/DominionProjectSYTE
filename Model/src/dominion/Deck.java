package dominion;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Digaly on 23/03/2016.
 */
public class Deck
{
    private ArrayList<Card> cards;

    public Deck ()
    {
        cards = new ArrayList<Card>();
    }

    public void makeHand(Deck deck, Deck discardPile)
    {
        cards = new ArrayList<Card>();
        for (int i = 0; i < 5; i++)
        {
            this.takeTopCard(deck, discardPile);
        }
    }

    public void takeTopCard(Deck deck, Deck discardPile)
    {
        ArrayList<Card> deckCards = deck.cards;
        if (deckCards.size() == 0)
        {
            deck.discardToDeck(discardPile);
        }
        Card topCard = deckCards.get(deckCards.size() - 1);
        deckCards.remove(deckCards.size() - 1);

        cards.add(topCard);
    }

    public Card getTopCard()
    {
        return cards.get(cards.size() - 1);
    }

    public Card findCard(String name)
    {
        Card card = null;

        for (Card c : cards)
        {
            if (c.getName().equals(name))
                card = c;
        }

        return card;
    }

    public int calculatePoints()
    {
        int points = 0;
        for (int cardnr = 0; cardnr < cards.size(); cardnr++)
        {
            //points += cards[cardnr].getValue();
        }

        return points;
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

    public Boolean checkHandForType(int type)
    {
        Boolean hasType = false;
        for (Card c : cards)
        {
            if (c.getType() == type)
            {
                hasType = true;
            }
        }
        return hasType;
    }

    public boolean containsActionCards()
    {
        boolean out = false;

        for (int i = 0; i < cards.size(); i++)
        {
            int type = cards.get(i).getType();

            if (type == 3 || type == 4 || type == 5) out = true;
        }

        return out;
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

    public void removeCard(Card c)
    {
        cards.remove(c);
    }

    public ArrayList<Card> getCards()
    {
        return cards;
    }

}
