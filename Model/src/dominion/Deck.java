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
        cards = new ArrayList<>();
    }

    public void makeHand(Deck hand, Deck discardPile)
    {
        hand.cards = new ArrayList<>();
        for (int i = 0; i < 5; i++)
        {
            this.takeTopCard(hand, discardPile);
        }
    }

    public void takeTopCard(Deck hand, Deck discardPile) //ALWAYS USE ON deck!
    {
        putTopCardIn(hand.getCards(), discardPile);
    }

    public void putTopCardIn (ArrayList<Card> cardList, Deck discardPile) //ALWAYS USE ON deck!
    {
        if (size() == 0)
        {
            discardToDeck(discardPile);
        }

        Card topCard = getTopCard(discardPile);
        cards.remove(size() - 1);

        cardList.add(topCard);
    }

    public Card getTopCard(Deck discardPile)
    {
        if (size() == 0)
        {
            discardToDeck(discardPile);
        }
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

    public void shuffle()
    {
        Collections.shuffle(cards);
    }

    private void discardToDeck(Deck discardPile)
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
            if (cards.get(i).isActionCard())
            {
                out = true;
            }
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
