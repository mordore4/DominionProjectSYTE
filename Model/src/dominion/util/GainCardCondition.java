package dominion.util;

import dominion.Card;
import dominion.Deck;
import dominion.Player;

import java.util.ArrayList;

/**
 * Created by Digaly on 20/05/2016.
 */
public class GainCardCondition extends Condition
{
    private int cost;
    private transient ArrayList<Card> startHand;

    public GainCardCondition(Player player, int cost)
    {
        super(player);

        this.startHand = (ArrayList<Card>) player.getHand().getCards().clone();
        this.cost = cost;
    }

    public int getCost()
    {
        return cost;
    }

    @Override
    public boolean isFulfilled()
    {
        Card newCard = null;
        ArrayList<Card> currentHand = getPlayer().getHand().getCards();

        for (Card card : currentHand)
        {
            if (!startHand.contains(card))
            {
                newCard = card;
            }
        }

        if (newCard != null)
        {
            return newCard.getCost() <= cost;
        }
        else
        {
            return false;
        }
    }
}
