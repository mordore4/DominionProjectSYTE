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
    private int amount;
    private transient ArrayList<Card> startHand;

    public GainCardCondition(Player player, int amount)
    {
        super(player);

        this.startHand = player.getHand().getCards();
        this.amount = amount;
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
            return newCard.getAmount() <= amount;
        }
        else
        {
            return false;
        }
    }
}
