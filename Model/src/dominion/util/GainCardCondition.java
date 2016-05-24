package dominion.util;

import dominion.Card;
import dominion.Deck;
import dominion.Game;
import dominion.Player;

import java.util.ArrayList;

/**
 * Created by Digaly on 20/05/2016.
 */
public class GainCardCondition extends Condition
{
    private int cost;
    private transient ArrayList<Card> startDiscardPile;
    private transient Game game;

    public GainCardCondition(Player player, Game game, int cost)
    {
        super(player);

        this.startDiscardPile = (ArrayList<Card>) player.getDiscardPile().getCards().clone();
        this.cost = cost;
        this.game = game;
    }

    public int getCost()
    {
        return cost;
    }

    @Override
    public boolean isFulfilled()
    {
        Card newCard = null;
        ArrayList<Card> currentDiscardPile = getPlayer().getDiscardPile().getCards();

        for (Card card : currentDiscardPile)
        {
            if (!startDiscardPile.contains(card))
            {
                newCard = card;
            }
        }

        if (newCard != null)
        {
            return (newCard.getCost() <= cost);
        }
        else
        {
            return false;
        }
    }
}
