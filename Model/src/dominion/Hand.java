package dominion;

import java.util.ArrayList;

/**
 * Created by Sam on 23/03/2016.
 */
public class Hand
{
    private ArrayList<Card> cards;

    public Hand(Deck deck, Deck discardPile)
    {
        cards = new ArrayList<Card>();

        for (int i = 0; i < 5; i++)
        {
            cards.add(deck.takeTopCard(discardPile));
        }
    }

    public void addCard(Card card)
    {
        cards.add(card);
    }

    public Card findCard(String name)
    {
        Card c = null;

        for (int i = 0; i < cards.size(); i++)
        {
            Card currentCard = cards.get(i);

            if (currentCard.getName().equals(name))
            {
                c = currentCard;
            }
        }

        return c;
    }

    public void removeCard(Card c)
    {
        cards.remove(c);
    }

    public void discard(Deck pile, String name)
    {

    }

    public void trash()
    {
    }

    public void reveal()
    {

    }

    public ArrayList<Card> getCards()
    {
        return cards;
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

}
