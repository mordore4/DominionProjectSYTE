package dominion;

import java.util.ArrayList;

/**
 * Created by Sam on 23/03/2016.
 */
public class Hand
{
    private ArrayList<Card> cards;

    public Hand()
    {
        //startkaarten hier <3
    }

    public void addCard(Card card)
    {
        cards.add(card);
    }

    public void discard(DiscardPile pile, String name)
    {

    }

    public void trash()
    {
    }

    public void reveal()
    {

    }
}
