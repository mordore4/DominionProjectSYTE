package dominion.util;

import dominion.Card;
import dominion.Game;
import dominion.Player;

import java.util.ArrayList;

/**
 * Created by Digaly on 25/05/2016.
 */
public class GainCardToHandCondition extends Condition
{
    private int cost;
    private transient ArrayList<Card> startHand;
    private transient Game game;
    private int type;

    public GainCardToHandCondition(Player player, Game game, int cost)
    {
        super(player);

        this.startHand = (ArrayList<Card>) player.getHand().getCards().clone();
        this.cost = cost;
        this.game = game;
        this.type = 0;
    }

    public GainCardToHandCondition(Player player, Game game, int cost, int type)
    {
        this(player, game, cost);

        this.type = type;
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
            if (type == 0)
            {
                return (newCard.getCost() <= cost);
            }
            else
            {
                return (newCard.getCost() <= cost && newCard.getType() == type);
            }
        }
        else
        {
            return false;
        }
    }

    public int getType()
    {
        return type;
    }
}
